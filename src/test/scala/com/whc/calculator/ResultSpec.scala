package com.whc.calculator

import org.scalacheck.Prop._
import org.scalacheck._

object ResultSpec extends Properties("Result") {
 
  def resultGen: Gen[Result[Int]] = for {
    n <- Gen.oneOf(-100, 100)
    result <- Gen.oneOf(Ok(n), Fail[Int](NotEnoughInput))
  } yield result

  // property("Result#fold should apply fail function if result is error or success if result is value") =
  //   forAll(resultGen) {
  //     case result@Fail(_) => result.fold(_ => 0, x => x) == 0
  //     case result@Ok(a) => result.fold(_ => 0, x => x) == a
  //   }

  // property("Result#map should apply passed function on a successful result and wrap it inside OK") =
  //   forAll(resultGen)(result => result match {
  //     case Fail(e) => result.map(_ => 1) == Fail(e)
  //     case Ok(a) => result.map(_ => 1) == Ok(1)
  //   })

  // property("Result#flatmap should apply passed funciton on a successful result") =
  //   forAll(resultGen) {
  //     case result@Fail(e) => result.flatMap(_ => Ok(1)) == Fail(e)
  //     case result@Ok(a) => result.flatMap(_ => Ok(1)) == Ok(1) &&
  //       result.flatMap(_ => Fail[Int](UnexpectedInput("?"))) == Fail(UnexpectedInput("?"))
  //   }
}  
