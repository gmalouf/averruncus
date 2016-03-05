package com.garymalouf.averruncus.password

import com.garymalouf.averruncus.{ Rule, RuleRunner }
import scala.language.higherKinds
import scalaz.{ Applicative, NonEmptyList, Validation, ValidationNel }

object Rules {
  type StringNel = NonEmptyList[String]

  def minLength[E: ErrorProvider](min: Int): Rule[E, String] =
    Rule(ErrorProvider[E].minLengthError(min), (_: String).length >= min)

  def maxLength[E: ErrorProvider](max: Int): Rule[E, String] =
    Rule(ErrorProvider[E].maxLengthError(max), (_: String).length <= max)
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

}
