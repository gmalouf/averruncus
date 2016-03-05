package com.garymalouf.averruncus

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import runners._
import scalaz.std.string._
import scalaz.std.option._
import scalaz.{ \/, -\/, \/-, Validation, Success, Failure }

class RuleRunnerSpec extends Specification with ScalaCheck {

  val passRule: Gen[Rule[String, String]] =
    Gen.alphaStr.map(Rule(_, (x: String) => true))
  val failRule: Gen[Rule[String, String]] =
    Gen.alphaStr.map(Rule(_, (x: String) => false))

  def nonEmpty[A](g: Gen[A]): Gen[Seq[A]] =
    for {
      s <- Gen.choose(1, 100)
      l <- Gen.listOfN(s, g)
    } yield l

  type SV[A] = Validation[String, A]
  type SD[A] = \/[String, A]
  "The RuleRunner" should {
    "use provided runner and reduce result of rules using Apply keepRight" >>
      forAll(Gen.alphaStr, nonEmpty(passRule), nonEmpty(failRule)) {
        (i, pass, fail) =>
          RuleRunner.run[String, String, Option](pass: _*)(i) mustEqual Some(())
          RuleRunner.run[String, String, Option](fail: _*)(i) mustEqual None
          RuleRunner.run[String, String, Option]((pass ++ fail): _*)(i) mustEqual None
          RuleRunner.run[String, String, SV](pass: _*)(i) mustEqual Success(())
          RuleRunner.run[String, String, SV](fail: _*)(i) mustEqual (
            Failure(fail.map(_.error).mkString)
          )
          RuleRunner.run[String, String, SV]((pass ++ fail): _*)(i) mustEqual (
            Failure(fail.map(_.error).mkString)
          )
          RuleRunner.run[String, String, SD](pass: _*)(i) mustEqual \/-(())
          RuleRunner.run[String, String, SD](fail: _*)(i) mustEqual (
            -\/(fail.head.error)
          )
          RuleRunner.run[String, String, SD]((pass ++ fail): _*)(i) mustEqual (
            -\/(fail.head.error)
          )
      }
  }
}
