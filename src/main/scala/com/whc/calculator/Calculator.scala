
package com.whc.calculator

object Calculator {

  sealed trait Operation

  case object Plus extends Operation

  case object Minus extends Operation

  case object Multiply extends Operation

  def calculate(op: Operation, n: Int, m: Int): Result[Int] = op match {
    case Plus => Ok(n + m)
    case Minus => Ok(n - m)
    case Multiply => Ok(n * m)
    case other => Fail(InvalidOperation(other.toString))
  }

  def operationParser: Parser[(Operation, Int, Int)] =
    for {
      n <- Parser.natural
      _ <- Parser.list(Parser.space)
      op <- Parser.operation
      _ <- Parser.list(Parser.space)
      a <- Parser.natural
    } yield (op, n, a)

  def run(operation: String): Result[Int] = {
    operationParser.run(operation) match {
      case Ok(ParseState(_, res)) => calculate(res._1, res._2, res._3)
      case Fail(error) => Fail(error)
    }
  }
}