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

trait PasswordValidator {
  import scalaz.syntax.std.boolean._

  def validateMinLength(requiredMinLength: Int)(password: String): Validation[String, Unit] =
    (password.length >= requiredMinLength).either(()).or(s"Password must contain at " +
      s"least $requiredMinLength characters").validation

  def validateMaxLength(requiredMaxLength: Int)(password: String): Validation[String, Unit] =
    (password.length <= requiredMaxLength).either(()).or(s"Password cannot contain more than " +
      s"$requiredMaxLength characters").validation
}

import scalaz.syntax.traverse1._
import scalaz.std.list._
object PasswordValidator extends PasswordValidator {
  //TODO: Combinations based on configuration
  def validatePassword(minPasswordLength: Int = 10, maxPasswordLength: Int = 50)(password: String): ValidationNel[String, Unit] = {
    val checks = List(validateMinLength(minPasswordLength) _, validateMaxLength(maxPasswordLength) _)

    checks.map(_.apply(password).toValidationNel).sequenceU.map(_ => Unit)
  }

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
