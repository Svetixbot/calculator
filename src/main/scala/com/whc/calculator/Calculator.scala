package com.whc.calculator

sealed trait Error

case class NotANumber(s: String) extends Error

case class InvalidOperation(s: String) extends Error

case class UnexpectedInput(s: String) extends Error

case object NotEnoughInput extends Error

/*
 * A result type that represents one of our errors or a success.
 */
case class Fail[A](error: Error) extends Result[A]

case class Ok[A](value: A) extends Result[A]

sealed trait Result[A] {
  /*
   * Exercise 1:
   *
   * We often want to work with data structures by breaking them
   * down by cases. With lists, this operation is foldRight. For
   * our result type this is just called fold. More formally we
   * refer to this as a catamorphism. Implement fold for Result.
   *
   * Hint: Try using pattern matching.
   *
   * scala> Ok(1).fold(_ => 0, x => x)
   *  = 1
   *
   * scala> Fail[Int](NotEnoughInput).fold(_ => 0, x => x)
   *  = 0
   */
  def fold[X](fail: Error => X, ok: A => X): X = this match {
    case Fail(error) => fail(error)
    case Ok(a) => ok(a)
  }


  /*
   * Exercise 2:
   *
   * Implement map for Result[A].
   *
   * The following laws must hold:
   *  1) r.map(z => z) == r
   *  2) r.map(z => f(g(z))) == r.map(g).map(f)
   *
   * scala> Ok(1).map(x => x + 10)
   *  = Ok(11)
   *
   * scala> Fail[Int](NotEnoughInput).map(x => x + 10)
   *  = Fail(NotEnoughInput)
   *
   * Advanced: Try using flatMap.
   */
  def map[B](f: A => B): Result[B] =
    fold(Fail[B], a => Ok(f(a)))

  /*
   * Exercise 3:
   *
   * Implement flatMap (a.k.a. bind, a.k.a. >>=).
   *
   * The following law must hold:
   *   r.flatMap(f).flatMap(g) == r.flatMap(z => f(z).flatMap(g))
   *
   * scala> Ok(1).flatMap(x => Ok(x + 10))
   *  = Ok(11)
   *
   * scala> Ok(1).flatMap(x => Fail[Int](NotEnoughInput))
   *  = Fail(NotEnoughInput)
   *
   * scala> Fail[Int](NotEnoughInput).flatMap(x => Ok(x + 10))
   *  = Fail(NotEnoughInput)
   *
   * scala> Fail[Int](NotEnoughInput).flatMap(x => Fail[Int](UnexpectedInput("?")))
   *  = Fail(NotEnoughInput)
   *
   * Advanced: Try using fold.
   */
  def flatMap[B](f: A => Result[B]): Result[B] =
    fold(Fail[B], f)
}

object Calculator {
  /** Simplified calculation data type. */
  sealed trait Operation

  case object Plus extends Operation

  case object Minus extends Operation

  case object Multiply extends Operation

  /*
   * Parse an int if it is valid, otherwise fail with NotAnInt.
   *
   * Hint: Scala defines String#toInt, but warning it throws exceptions
   *       if it is not a valid Int :| i.e. use try catch.
   */
  def int(body: String): Result[Int] = {
    try {
      Ok(body.toInt)
    } catch {
      case ex: Exception => Fail(NotANumber("Not a number"))
    }
  }

  /*
   * Parse the operation if it is valid, otherwise fail with InvalidOperation.
   */
  def operation(op: String): Result[Operation] = op match {
    case "+" => Ok(Plus)
    case "-" => Ok(Minus)
    case "*" => Ok(Multiply)
    case _ => Fail(InvalidOperation("Invalid Operation"))
  }

  /*
   * Compute an `answer`, by running operation for n and m.
   */
  def calculate(op: Operation, n: Int, m: Int): Int = op match {
    case Plus => n + m
    case Minus => n - m
    case Multiply => n * m
  }

  /*
   * Attempt to compute an `answer`, by:
   *  - parsing operation
   *  - parsing n
   *  - parsing m
   *  - running calculation
   *
   * hint: use flatMap / map
   */
  def attempt(op: String, n: String, m: String): Result[Int] =
    for {
      op1 <- operation(op)
      n1 <- int(n)
      m1 <- int(m)
    } yield calculate(op1, n1, m1)

  /*
   * Run a calculation by pattern matching three elements off the input arguments,
   * parsing the operation, a value for n and a value for m.
   */
  def run(args: List[String]): Result[Int] = {
    if (args.length != 3)
      Fail(UnexpectedInput(""))
    else
      attempt(args(0), args(1), args(2))
  }

  def main(args: Array[String]) =
    println(run(args.toList) match {
      case Ok(result) => s"result: ${result}"
      case Fail(error) => s"failed: ${error}"
    })
}
