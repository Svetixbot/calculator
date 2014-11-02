package com.whc.calculator

import org.scalatest.{Ignore, FunSpec}

class CalculatorSpec extends FunSpec {
  describe("A Calculator") {
    ignore("should add two numbers") {
      assert(Calculator.run("1+2") === Ok(3))
    }

    ignore("should subtract two numbers") {
      assert(Calculator.run("3-2") === Ok(1))
    }

    ignore("should multiply a number by another number") {
      assert(Calculator.run("3*2") === Ok(6))
    }

    ignore("should fail if the input contains only one operand"){
      assert(Calculator.run("3") === Fail(NotEnoughInput))
    }

    ignore("should fail if we have only the first operand & operator"){
      assert(Calculator.run("2+") === Fail(NotEnoughInput))
    }

    ignore("should fail if one of the operands is not a number"){
      assert(Calculator.run("2+a") === Fail(UnexpectedInput("a")))
    }

    ignore("should calculate if there are spaces"){
      assert(Calculator.run("2 + 2") === Ok(4))
    }

    ignore("should fail if there are spaces at the beginning and at the end"){
      assert(Calculator.run(" 2 + 2 ") === Fail(UnexpectedInput(" 2 + 2 ")))
    }

    ignore("should fail if the operator is not supported"){
      assert(Calculator.run("2%2") === Fail(InvalidOperation("%")))
    }

  }
}
