package com.garymalouf.averruncus.password

import com.garymalouf.averruncus.{ Rule, RuleRunner }
import scala.language.higherKinds
import scalaz.{ Applicative, NonEmptyList, Validation, ValidationNel }

object Rules {
  type StringNel = NonEmptyList[String]

  def minLengthError(min: Int) =
    s"Password must contain at least ${min} characters"

  def minLengthE(min: Int)(error: String = minLengthError(min)): Rule[String, StringNel] =
    Rule(NonEmptyList(error), (_: String).length >= min)

  def maxLengthError(max: Int) =
    s"Password cannot contain more than ${max} characters"

  def maxLengthE(max: Int)(error: String = maxLengthError(max)): Rule[String, StringNel] =
    Rule(NonEmptyList(error), (_: String).length <= max)

  //convenience
  def minLength(min: Int) = minLengthE(min)()
  def maxLength(max: Int) = maxLengthE(max)()
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
  type Runner[G[_]] = RuleRunner.Runner[Boolean, Rules.StringNel, G]

  def validate[G[_]: Applicative](rules: Rule[String, Rules.StringNel]*)(pw: String)(
    implicit
    runner: Runner[G]
  ): G[Unit] =
    RuleRunner.run[String, Rules.StringNel, G](rules: _*)(pw)

}
