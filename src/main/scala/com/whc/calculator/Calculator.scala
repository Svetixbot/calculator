package com.whc.calculator

object Calculator {

  sealed trait Operation

  case object Plus extends Operation

  case object Minus extends Operation

  case object Multiply extends Operation

  def operationParser: Parser[(Char, Int, Int)] = ???

  def run(operation: String): Result[Int] = {
    operationParser.run(operation) match {
      case Ok(ParseState(_, res)) => attempt(res._1, res._2, res._3)
      case Fail(error) => Fail(error)
    }
  }
}
