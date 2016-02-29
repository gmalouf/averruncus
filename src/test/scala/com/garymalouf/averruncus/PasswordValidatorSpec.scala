package com.garymalouf.averruncus

import org.scalacheck.{ Arbitrary, Gen }
import Arbitrary._
import org.specs2.ScalaCheck
import org.specs2.matcher.ValidationMatchers
import org.specs2.mutable.Specification

class PasswordValidatorSpec extends Specification with ScalaCheck with ValidationMatchers {
  import PasswordValidator._

  "The validateMinLength function" should {
    "return a success when the password meets requirements" >> prop {
      (testCase: MinLengthStringTestCase) =>

        validateMinLength(testCase.lengthConstraint)(testCase.passwordUnderTest) must beSuccess
    }

    "return a failure when the password is too short" >> prop {
      (testCase: MinLengthStringTestCase) =>

        validateMinLength(testCase.lengthConstraint)(testCase.passwordUnderTest) must beFailure
    }.setGen(minLengthStringNegTestGen)
  }

  "The validateMaxLength function" should {
    "return a success when the password meets requirements" >> prop {
      (testCase: MaxLengthStringTestCase) =>
        validateMaxLength(testCase.lengthConstraint)(testCase.passwordUnderTest) must beSuccess
    }

    "return a failure when the password is too long" >> prop {
      (testCase: MaxLengthStringTestCase) =>

        validateMaxLength(testCase.lengthConstraint)(testCase.passwordUnderTest) must beFailure
    }.setGen(maxLengthStringNegTestGen)
  }

  case class MinLengthStringTestCase(lengthConstraint: Int, passwordUnderTest: String)

  // Need to prevent OOM Error
  val GlobalMaxStringLength = 50

  def minLengthStringPosTestGen: Gen[MinLengthStringTestCase] =
    for {
      minStringLength <- Gen.choose(1, GlobalMaxStringLength)
      testPasswordLength <- Gen.choose(minStringLength, GlobalMaxStringLength)
      testPassword <- Gen.listOfN(testPasswordLength, arbitrary[Char]).map(_.mkString)
    } yield MinLengthStringTestCase(minStringLength, testPassword)

  def minLengthStringNegTestGen: Gen[MinLengthStringTestCase] =
    for {
      minStringLength <- Gen.choose(1, GlobalMaxStringLength)
      testPasswordLength <- Gen.choose(0, minStringLength - 1)
      testPassword <- Gen.listOfN(testPasswordLength, arbitrary[Char]).map(_.mkString)
    } yield MinLengthStringTestCase(minStringLength, testPassword)

  implicit val minLengthStringTestCaseArb = Arbitrary(minLengthStringPosTestGen)

  case class MaxLengthStringTestCase(lengthConstraint: Int, passwordUnderTest: String)
  def maxLengthStringPosTestGen: Gen[MaxLengthStringTestCase] =
    for {
      maxStringLength <- Gen.choose(2, GlobalMaxStringLength)
      testPasswordLength <- Gen.choose(1, maxStringLength)
      testPassword <- Gen.listOfN(testPasswordLength, arbitrary[Char]).map(_.mkString)
    } yield MaxLengthStringTestCase(maxStringLength, testPassword)

  def maxLengthStringNegTestGen: Gen[MaxLengthStringTestCase] =
    for {
      maxStringLength <- Gen.choose(2, GlobalMaxStringLength)
      testPasswordLength <- Gen.choose(maxStringLength + 1, maxStringLength + 10)
      testPassword <- Gen.listOfN(testPasswordLength, arbitrary[Char]).map(_.mkString)
    } yield MaxLengthStringTestCase(maxStringLength, testPassword)

  implicit val maxLengthStringTestCaseArb = Arbitrary(maxLengthStringPosTestGen)
}
