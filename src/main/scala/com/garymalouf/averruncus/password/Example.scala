package com.garymalouf.averruncus.password

import com.garymalouf.averruncus.password.Rules._
import com.garymalouf.averruncus.runners._
import com.garymalouf.averruncus.password.PasswordValidator.PasswordValidation
import scalaz.{ \/, NonEmptyList }
import scalaz.std.option._

object Example extends App {

  //totally ridiculous contrived example to show accumulation
  val pwv =
    PasswordValidator.validate[PasswordValidation](
      minLength(3),
      maxLength(1)
    )("pw")

  type SD[A] = \/[NonEmptyList[String], A]
  val pdj =
    PasswordValidator.validate[SD](
      minLength(3),
      maxLength(1)
    )("pw")

  val po =
    PasswordValidator.validate[Option](
      minLength(3),
      maxLength(1)
    )("pw")

  val success =
    PasswordValidator.validate[PasswordValidation](
      minLength(3), maxLength(5)
    )("pass")

  println(pwv)
  println(pdj)
  println(po)
  println(success)
}
