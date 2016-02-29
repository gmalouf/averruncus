package com.garymalouf.averruncus

import scalaz.{ Validation, ValidationNel }

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

}
