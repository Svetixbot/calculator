package com.whc.calculator

import com.whc.calculator.Calculator._
import org.scalacheck.Prop._
import org.scalacheck._

object ParserSpec extends Properties("Parser") {

  def input: Gen[String] = for {
    number <- Gen.choose(0, 100)
    numericString <- Gen.oneOf(number.toString, "")
    string <- Gen.alphaStr
  } yield numericString + string

  property("Parser#map returns a parser with the function `f` applied to the output of that parser") =
   forAll(input) { input => 
      forAll((f: Int => String) => {
        val parser = Parser.natural
        (parser.run(input), parser.map(f).run(input)) match {
          case (Ok(ParseState(r,p)), Ok(ParseState(rest, parsed))) => r == rest && parsed == f(p)
          case (fail1@_, fail2@_) => fail1 == fail2
        }
      })    
  }

  def spaceInput: Gen[String] = for {
    s <- Gen.alphaStr
    result <- Gen.oneOf(" " + s, s)
  } yield result

  def operationInput = for {
    s <- Gen.alphaStr
    op <- Gen.oneOf("*", "+", "-", "")
  } yield op + s

  property("Parser#character returns the first char from the input") =
    forAll(input) { input =>
      if (input.isEmpty) {
        Parser.character.run(input) == Fail(NotEnoughInput)
      }
      else {
        Parser.character.run(input) == Ok(ParseState(input.substring(1), input.charAt(0)))
      }
    }

  property("Parser#digit returns the first digit from the input") =
    forAll(input) { input =>
      val digit = """(^\d)(.*)""".r
      input match{
        case "" =>  Parser.digit.run(input) == Fail(NotEnoughInput)
        case digit(d,s) => Parser.digit.run(input) == Ok(ParseState(s, d.charAt(0)))
        case _ =>   Parser.digit.run(input) == Fail(NotANumber(input.charAt(0).toString))
      }
    }

 property("Parser#natural returns the first natural number from the input") =
   forAll(input) { input => 
     val natural = """(^\d+)(.*)""".r
     input match{
       case "" =>  Parser.natural.run(input) == Fail(NotEnoughInput)
       case natural(n,s) => Parser.natural.run(input) == Ok(ParseState(s, n.toInt))
       case _ => Parser.natural.run(input) == Fail(NotANumber(input.charAt(0).toString))
     }
   }

  property("Parser#space returns the first space") =
   forAll(spaceInput) { input => 
     val stringWithSpace = """(^\s+)(.*)""".r
     input match{
       case "" =>  Parser.space.run(input) == Fail(NotEnoughInput)
       case stringWithSpace(s,rest) => Parser.space.run(input) == Ok(ParseState(rest, s.charAt(0)))
       case _ => Parser.space.run(input) == Fail(UnexpectedInput(input.charAt(0).toString))
     }
   }

  property("Parser#operation returns the first operation") = 
   forAll(operationInput) { input => 
     if (input.isEmpty) {
       Parser.operation.run(input) == Fail(NotEnoughInput)
     } else {
      input.charAt(0) match {
        case '+' => Parser.operation.run(input) == Ok(ParseState(input.drop(1), Plus))
        case '-' => Parser.operation.run(input) == Ok(ParseState(input.drop(1), Minus))
        case '*' => Parser.operation.run(input) == Ok(ParseState(input.drop(1), Multiply))
        case _ => Parser.operation.run(input) == Fail(InvalidOperation(input.charAt(0).toString))      
      }
     } 
   }
}  
