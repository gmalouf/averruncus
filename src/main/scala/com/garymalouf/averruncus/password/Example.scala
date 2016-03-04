package com.garymalouf.averruncus.password

import com.garymalouf.averruncus.password.ErrorProviders._
import com.garymalouf.averruncus.password.PasswordValidator.PasswordValidation
import com.garymalouf.averruncus.password.Rules._
import com.garymalouf.averruncus.runners._
import scalaz.{ \/, NonEmptyList, Validation }
import scalaz.std.option._
import scalaz.std.string._

object Example extends App {

  //totally ridiculous contrived example to show accumulation
  val pwv =
    PasswordValidator.validate[StringNel, PasswordValidation](
      minLength(3),
      maxLength(1)
    )("pw")

  type PWV[A] = Validation[String, A]

  val pwsv =
    PasswordValidator.validate[String, PWV](
      minLength(3),
      maxLength(1)
    )("pw")

  type SD[A] = \/[NonEmptyList[String], A]
  val pdj =
    PasswordValidator.validate[StringNel, SD](
      minLength(3),
      maxLength(1)
    )("pw")

  val po =
    PasswordValidator.validate[StringNel, Option](
      minLength(3),
      maxLength(1)
    )("pw")

  val success =
    PasswordValidator.validate[StringNel, PasswordValidation](
      minLength(3), maxLength(5)
    )("pass")

  println(pwv)
  println(pwsv)
  println(pdj)
  println(po)
  println(success)
}
