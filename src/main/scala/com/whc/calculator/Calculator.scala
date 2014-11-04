package com.whc.calculator

/*
 * This is a calculator object
 * Use CalculatorSpec to drive your implementation
 */
object Calculator {

  sealed trait Operation

  case object Plus extends Operation

  case object Minus extends Operation

  case object Multiply extends Operation


  /*
   * This method makes the calculation itself
   * Based on operation return a result of calculation or an error if operaiton is not supported
   * 
   * Hint: try to use pattern matching
   */
  def calculate(op: Operation, n: Int, m: Int): Result[Int] = ???


  /*
   * This methods should return a parser of your operation
   * Combine the parsers you have written in a previose exercises to build your own parser
   *
   * Hint: try to use for comprehension
   * Advanced: try to translate this comprehension into a flatMap/map structure
   */
  def operationParser: Parser[(Operation, Int, Int)] = ???


  /*
   * This is a main method you invoke to calculate the expression
   * 
   * Here you will parse your operation and if it is successfull you will make a calculation
   * 
   * Hint: ask someone what tupple is and how to access values from it.
   */
  def run(operation: String): Result[Int] = ???
}
