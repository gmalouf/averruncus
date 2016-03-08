package com.garymalouf.averruncus.password

import scalaz.NonEmptyList

trait ErrorProvider[E] {
  def minLengthError(min: Int): E
  def maxLengthError(max: Int): E
  val hasUpper: E
  val hasLower: E
  val hasNumeric: E
  val hasSymbol: E
  val noWhitespace: E
  val consecIdenticalCharsError: E
}

object ErrorProvider {
  def apply[E: ErrorProvider] = implicitly[ErrorProvider[E]]
}

object ErrorProviders {

  implicit val string = new ErrorProvider[String] {
    def minLengthError(min: Int) =
      s"Password must contain at least $min characters"
    def maxLengthError(max: Int) =
      s"Password cannot contain more than $max characters"
    val hasUpper =
      "Password must contain at least one uppercase character"
    val hasLower =
      "Password must contain at least one lowercase character"
    val hasNumeric =
      "Password must contain at least one numeric character"
    val hasSymbol =
      "Password must contain at least one non-alphanumeric character"
    val noWhitespace =
      "Password must not contain any whitespace characters"
    val consecIdenticalCharsError =
      "Password must not contain more than 3 identical characters in a row"
  }

  implicit val stringNel = new ErrorProvider[Rules.StringNel] {
    def minLengthError(min: Int) =
      NonEmptyList(string.minLengthError(min))
    def maxLengthError(max: Int) =
      NonEmptyList(string.maxLengthError(max))
    val hasUpper =
      NonEmptyList(string.hasUpper)
    val hasLower =
      NonEmptyList(string.hasLower)
    val hasNumeric =
      NonEmptyList(string.hasNumeric)
    val hasSymbol =
      NonEmptyList(string.hasSymbol)
    val noWhitespace =
      NonEmptyList(string.noWhitespace)
    val consecIdenticalCharsError =
      NonEmptyList(string.consecIdenticalCharsError)
  }
}
