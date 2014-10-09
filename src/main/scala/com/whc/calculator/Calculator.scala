package com.whc.calculator

object Calculator {

  sealed trait Operation

  case object Plus extends Operation

  case object Minus extends Operation

  case object Multiply extends Operation

  def operation(op: Char): Result[Operation] = op match {
    case '+' => Ok(Plus)
    case '-' => Ok(Minus)
    case '*' => Ok(Multiply)
    case _ => Fail(InvalidOperation("Invalid Operation"))
  }

  def calculate(op: Operation, n: Int, m: Int): Int = op match {
    case Plus => n + m
    case Minus => n - m
    case Multiply => n * m
  }

  def attempt(op: Char, n: Int, m: Int): Result[Int] =
    for {
      op1 <- operation(op)
    } yield calculate(op1, n, m)

  def operationParser: Parser[(Char, Int, Int)] =
    for {
      n <- Parser.natural
      _ <- Parser.list(Parser.space)
      op <- Parser.character
      _ <- Parser.list(Parser.space)
      a <- Parser.natural
    } yield new Tuple3(op, n, a)
  
  def run(operation: String): Result[Int] = {
      operationParser.run(operation) match {
        case Ok(ParseState(_,res)) => attempt(res._1,res._2,res._3)
        case Fail(error) => Fail(error)
      }
  }
}
