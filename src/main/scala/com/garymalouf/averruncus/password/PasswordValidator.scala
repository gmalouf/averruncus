package com.garymalouf.averruncus.password

import com.garymalouf.averruncus.{ Rule, RuleRunner }
import scala.language.higherKinds
import scala.util.matching.Regex
import scalaz.{ Applicative, NonEmptyList, Validation, ValidationNel }

object Rules {
  type StringNel = NonEmptyList[String]

  def minLength[E: ErrorProvider](min: Int): Rule[E, String] =
    Rule(ErrorProvider[E].minLengthError(min), (_: String).length >= min)

  def maxLength[E: ErrorProvider](max: Int): Rule[E, String] =
    Rule(ErrorProvider[E].maxLengthError(max), (_: String).length <= max)

  def regexPosTest[E: ErrorProvider](e: E, r: Regex): Rule[E, String] =
    Rule(e, (s: String) => r.findFirstIn(s).nonEmpty)

  val upperR = "(?=.*[A-Z])".r

  def hasUpper[E: ErrorProvider]: Rule[E, String] =
    regexPosTest(ErrorProvider[E].hasUpper, upperR)

  val lowerR = "(?=.*[a-z])".r

  def hasLower[E: ErrorProvider]: Rule[E, String] =
    regexPosTest(ErrorProvider[E].hasLower, lowerR)

  val numR = "(?=.*[0-9])".r

  def hasNumeric[E: ErrorProvider]: Rule[E, String] =
    regexPosTest(ErrorProvider[E].hasNumeric, numR)

  val symR = """(?=.*[\W])""".r

  def hasSymbol[E: ErrorProvider]: Rule[E, String] =
    regexPosTest(ErrorProvider[E].hasSymbol, symR)

  val wsR = """^\S*$""".r

  def noWhitespace[E: ErrorProvider]: Rule[E, String] =
    regexPosTest(ErrorProvider[E].noWhitespace, wsR)

  val dcR = """(.)\1\1+""".r
  // Ensures that the same character does not show up 3 times in a row or more
  def limitConsecutiveIdenticalCharacters[E: ErrorProvider]: Rule[E, String] =
    Rule(ErrorProvider[E].consecIdenticalCharsError, dcR.findFirstIn(_).isEmpty)

}

object PasswordValidator {

  type PasswordValidation[A] = ValidationNel[String, A]
  type Runner[E, G[_]] = RuleRunner.Runner[Boolean, E, G]

  def validate[E: ErrorProvider, G[_]: Applicative](rules: Rule[E, String]*)(
    pw: String
  )(
    implicit
    runner: Runner[E, G]
  ): G[Unit] =
    RuleRunner.run[E, String, G](rules: _*)(pw)

  def validateAll[E: ErrorProvider, G[_]: Applicative](minLength: Int, maxLength: Int)(
    pw: String
  )(
    implicit
    runner: Runner[E, G]
  ): G[Unit] =
    validate[E, G](
      Rules.minLength[E](minLength),
      Rules.maxLength[E](maxLength),
      Rules.hasUpper[E],
      Rules.hasLower[E],
      Rules.hasNumeric[E],
      Rules.hasSymbol[E],
      Rules.noWhitespace[E],
      Rules.limitConsecutiveIdenticalCharacters[E]
    )(pw)
}
