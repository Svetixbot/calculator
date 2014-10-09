package com.whc.calculator

import org.scalatest.FunSpec

class CalculatorSpec extends FunSpec {

  describe("A Calculator") {
    it("does somthing") {
      assert(Calculator.run(List("+", "1", "2")) === Ok(3))
    }
  }
}
