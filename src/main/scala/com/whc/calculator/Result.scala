package com.whc.calculator

sealed trait Error

case class NotANumber(s: String) extends Error

case class InvalidOperation(s: String) extends Error

case class UnexpectedInput(s: String) extends Error

case object NotEnoughInput extends Error

/*
 * A result type that represents one of our errors or a success.
 */
case class Fail[A](error: Error) extends Result[A]

case class Ok[A](value: A) extends Result[A]

sealed trait Result[A] {
  /*
   * Exercise 1:
   *
   * We often want to work with data structures by breaking them
   * down by cases. With lists, this operation is foldRight. For
   * our result type this is just called fold. More formally we
   * refer to this as a catamorphism. Implement fold for Result.
   *
   * Hint: Try using pattern matching.
   *
   * scala> Ok(1).fold(_ => 0, x => x)
   *  = 1
   *
   * scala> Fail[Int](NotEnoughInput).fold(_ => 0, x => x)
   *  = 0
   */
  def fold[X](fail: Error => X, ok: A => X): X = ???

  /*
   * Exercise 2:
   *
   * Implement map for Result[A].
   *
   * The following laws must hold:
   *  1) r.map(z => z) == r
   *  2) r.map(z => f(g(z))) == r.map(g).map(f)
   *
   * scala> Ok(1).map(x => x + 10)
   *  = Ok(11)
   *
   * scala> Fail[Int](NotEnoughInput).map(x => x + 10)
   *  = Fail(NotEnoughInput)
   *
   * Advanced: Try using flatMap.
   */
  def map[B](f: A => B): Result[B] = ???

  /*
   * Exercise 3:
   *
   * Implement flatMap (a.k.a. bind, a.k.a. >>=).
   *
   * The following law must hold:
   *   r.flatMap(f).flatMap(g) == r.flatMap(z => f(z).flatMap(g))
   *
   * scala> Ok(1).flatMap(x => Ok(x + 10))
   *  = Ok(11)
   *
   * scala> Ok(1).flatMap(x => Fail[Int](NotEnoughInput))
   *  = Fail(NotEnoughInput)
   *
   * scala> Fail[Int](NotEnoughInput).flatMap(x => Ok(x + 10))
   *  = Fail(NotEnoughInput)
   *
   * scala> Fail[Int](NotEnoughInput).flatMap(x => Fail[Int](UnexpectedInput("?")))
   *  = Fail(NotEnoughInput)
   *
   * Advanced: Try using fold.
   */
  def flatMap[B](f: A => Result[B]): Result[B] = ???
}