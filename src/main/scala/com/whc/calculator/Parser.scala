package com.whc.calculator
/**
 * A data type that just makes it easier to work with parse states.
 *
 * This is effectively just a tuple, with named accessors.
 */
case class ParseState[A](input: String, value: A)

/**
 * A parser is a function from an input string,
 */
case class Parser[A](run: String => Result[ParseState[A]]) {
  /**
   * Return a parser with the function `f` applied to the
   * output of that parser.
   */
  def map[B](f: A => B): Parser[B] =
    Parser(input => run(input).map({
      case ParseState(i, v) => ParseState(i, f(v))
    }))

  /**
   * Return a parser that feeds its input into this parser, and
   *
   * - if that parser succeeds, apply its result to function f, and
   *   then run the resultant parser with the updated input
   *
   * - if that parser fails with an error, return a parser with
   *   that error
   */
  def flatMap[B](f: A => Parser[B]): Parser[B] =
    Parser(input => run(input) match {
      case Ok(ParseState(i,a)) => f(a).run(i)
      case Fail(e) => Fail(e)
    })
}

object Parser {
  /**
   * Return a parser that succeeds with a character off the input
   * or fails with NotEnoughInput if the input is empty.
   *
   * scala> Parser.character.run("hello")
   * = Ok(ParseState(ello, h))
   */
  def character: Parser[Char] =
    Parser(input => input.toList match {
      case c :: cs => Ok(ParseState(cs.mkString, c))
      case Nil => Fail(NotEnoughInput)
    })

  /**
   * Return a parser that continues producing a list of values from the
   * given parser.
   *
   * scala> Parser.list(Parser.character).run("hello")
   * = Ok(ParseState(,List(h,e,l,l,o)))
   *
   * scala> Parser.list(Parser.character).run("")
   * = Ok(ParseState(,List()))
   */
  def list[A](parser: Parser[A]): Parser[List[A]] =
    Parser {
      input =>
        def parse(cs: String, st: List[A]): (String, List[A]) =
          parser.run(cs) match {
            case Ok(ParseState(c, s)) => parse(c, s :: st)
            case Fail(_) => (cs, st)
          }
        parse(input, List()) match {
          case (remaining, parsed) => Ok(ParseState(remaining, parsed.reverse))
        }
    }

  /**
   * Return a parser that produces at least one value from the
   * given parser then continues producing a list of values from
   * the given parser (to ultimately produce a non-empty list).
   *
   * The returned parser fails if the input is empty.
   *
   * scala> Parser.list1(Parser.character).run("hello")
   * = Ok(ParseState(,List(h,e,l,l,o)))
   *
   * scala> Parser.list1(Parser.character).run("")
   * = Fail(NotEnoughInput)
   */
  def list1[A](parser: Parser[A]): Parser[List[A]] =
    Parser {
      input =>
        if (input.isEmpty)
          Fail(NotEnoughInput)
        else
          (for {
            head <- parser
            tail <- list(parser)
          } yield head :: tail).run(input)
          // parser.flatMap(head => list(parser).map(tail => head :: tail)).run(input)
    }

  /**
   * Return a parser that produces a character but fails if
   *
   * - The input is empty, or
   *
   * - The character does not satisfy the given predicate
   *
   * scala> Parser.satisfy(c => c == 'h').run("hello")
   * = Ok(ParseState(ello,h))
   */
  def satisfy(pred: Char => Boolean, me: Char => Error): Parser[Char] =
    Parser(input => character.run(input) match {
      case ok@Ok(ParseState(i, a)) =>
        if (pred(a))
          ok
        else
          Fail(me(a))
      case fail@Fail(e) => fail
    })

  /**
   * Return a parser that produces a character between '0' and '9'
   * but fails if
   *
   * - The input is empty, or
   *
   * - The produced character is not a digit
   *
   * scala> Parser.digit.run("123hello")
   * = Ok(ParseState(23hello,1))
   *
   * scala> Parser.digit.run("hello")
   * = Fail(UnexpectedInput(h))
   */
  def digit: Parser[Char] =
    satisfy(c => c.isDigit, c => NotANumber(c.toString))

  /**
   * Return a parser that produces zero or a positive integer but fails if
   *
   * - The input is empty, or
   *
   * - The input does not produce a value series of digits
   *
   * scala> Parser.natural.run("123hello")
   * = Ok(ParseState(hello, 123))
   *
   * scala> Parser.natural.run("hello")
   * = Fail(UnexpectedInput(h))
   */
  def natural: Parser[Int] =
    list1(digit).map(_.mkString.toInt)

  /**
   * Return a parser that produces a space character but fails if
   *
   * - The input is empty, or
   *
   * - The produced character is not a space
   *
   * scala> Parser.space.run(" hello")
   * = Ok(ParseState(hello, ))
   *
   * scala> Parser.space.run("hello")
   * = Fail(UnexpectedInput(h))
   */
  def space: Parser[Char] =
    satisfy(_.isSpaceChar, c=>UnexpectedInput(c.toString))
}