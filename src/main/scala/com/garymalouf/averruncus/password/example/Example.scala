package com.garymalouf.averruncus.password.example

import com.garymalouf.averruncus.password.ErrorProviders._
import com.garymalouf.averruncus.password.PasswordValidator
import com.garymalouf.averruncus.password.PasswordValidator.PasswordValidation
import com.garymalouf.averruncus.password.Rules._
import com.garymalouf.averruncus.runners._
import scalaz.{ \/, NonEmptyList, Semigroup, Validation }
import scalaz.std.option._

object Example extends App {

  val scapp: (String, => String) => String =
    (s1, s2) => s"${s1}, ${s2}"
  implicit val ssg =
    Semigroup.instance(scapp)

  //totally ridiculous contrived example to show accumulation
  val pwva =
    PasswordValidator.validateAll[StringNel, PasswordValidation](3, 1)("pw")

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

  println(pwva)
  println(pwv)
  println(pwsv)
  println(pdj)
  println(po)
  println(success)
}
