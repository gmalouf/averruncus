package com.garymalouf.averruncus.password

import scalaz.NonEmptyList

trait ErrorProvider[E] {
  def minLengthError(min: Int): E
  def maxLengthError(max: Int): E
}

object ErrorProvider {
  def apply[E: ErrorProvider] = implicitly[ErrorProvider[E]]
}

object ErrorProviders {

  implicit val string = new ErrorProvider[String] {
    def minLengthError(min: Int) =
      s"Password must contain at least ${min} characters"
    def maxLengthError(max: Int) =
      s"Password cannot contain more than ${max} characters"
  }

  implicit val stringNel = new ErrorProvider[Rules.StringNel] {
    def minLengthError(min: Int) =
      NonEmptyList(string.minLengthError(min))
    def maxLengthError(max: Int) =
      NonEmptyList(string.maxLengthError(max))
  }
}
