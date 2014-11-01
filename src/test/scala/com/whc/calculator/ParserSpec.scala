package com.whc.calculator

import org.scalacheck.Arbitrary._
import org.scalacheck.Prop._
import org.scalacheck._

object ParserSpec extends Properties("Parser") {

  def inputString: Gen[String] = for {
    number <- Gen.choose(0, 100)
    numericString <- Gen.oneOf(number.toString, "")
    string <- Gen.alphaStr
  } yield numericString + string

  property("Parser#character returns the first char from the input") =
    forAll(inputString) { input =>
      if (input.isEmpty) {
        Parser.character.run(input) == Fail(NotEnoughInput)
      }
      else {
        Parser.character.run(input) == Ok(ParseState(input.substring(1), input.charAt(0)))
      }
    }

  property("Parser#digit returns the first digit from the input") =
    forAll(inputString) { input =>
      val digit = """(^\d)(.*)""".r
      input match{
        case "" =>  Parser.digit.run(input) == Fail(NotEnoughInput)
        case digit(d,s) => Parser.digit.run(input) == Ok(ParseState(s, d.charAt(0)))
        case _ =>   Parser.digit.run(input) == Fail(NotANumber(input.charAt(0).toString))
      }
    }

 property("Parser#natural returns the first natural number from the input") =
   forAll(inputString) { input => 
     val natural = """(^\d+)(.*)""".r
     input match{
       case "" =>  Parser.natural.run(input) == Fail(NotEnoughInput)
       case natural(n,s) => Parser.natural.run(input) == Ok(ParseState(s, n.toInt))
       case _ => Parser.natural.run(input) == Fail(NotANumber(input.charAt(0).toString))
     }
   }
}
