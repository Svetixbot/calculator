package com.whc.calculator

import org.scalacheck._, Arbitrary._, Gen._, Prop._

object ParserSpec extends Properties("Parser") {
  property("Parser#character returns the first char from the input") =
    forAll((input: String) => {
      if (input.isEmpty) {
        Parser.character.run(input) == Fail(NotEnoughInput)
      }
      else {
        Parser.character.run(input) == Ok(ParseState(input.substring(1), input.charAt(0)))
      }
    })

  property("Parser#digit returns the first digit from the input") =
    forAll((input: String) => {
      input match{
        case ""=>  Parser.digit.run(input) == Fail(NotEnoughInput)
        case """"(\d+)"""=>Parser.digit.run(input) == Ok(ParseState(input.substring(1), input.charAt(0)))
        case _=>   Parser.digit.run(input) == Fail(NotANumber(input.charAt(0).toString))
      }
    })

  property("Parser#natural returns the first natural number from the input") =
    forAll((input: String) => {
      input match{
        case ""=>  Parser.natural.run(input) == Fail(NotEnoughInput)
        case """"(\d+)"""=> {

        //  Parser.natural.run(input) == Ok(ParseState(input.substring(1), input.substr(0)))
        }
        case _=>   Parser.natural.run(input) == Fail(NotANumber(input.charAt(0).toString))
      }
    })
}
