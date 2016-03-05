package com.garymalouf.averruncus.password

import ErrorProviders._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification

class RulesSpec extends Specification with ScalaCheck {

  val lengthsGen = for {
    m <- Gen.posNum[Int]
    l <- Gen.choose(0, m - 1)
    g <- Gen.choose(m + 1, m * 2)
    ms <- Gen.listOfN(m, arbitrary[Char]).map(_.mkString)
    ls <- Gen.listOfN(l, arbitrary[Char]).map(_.mkString)
    gs <- Gen.listOfN(g, arbitrary[Char]).map(_.mkString)
  } yield (m, ms, ls, gs)

  "The minLength rule" should {
    "return whether length of input is >= specified min" >>
      forAll(lengthsGen) { (ls) =>
        Rules.minLength[Rules.StringNel](ls._1).run(ls._2) must beTrue
        Rules.minLength[Rules.StringNel](ls._1).run(ls._3) must beFalse
        Rules.minLength[Rules.StringNel](ls._1).run(ls._4) must beTrue
      }
  }

  "The maxLength rule" should {
    "return whether length of input is <= specified max" >>
      forAll(lengthsGen) { (ls) =>
        Rules.maxLength[Rules.StringNel](ls._1).run(ls._2) must beTrue
        Rules.maxLength[Rules.StringNel](ls._1).run(ls._3) must beTrue
        Rules.maxLength[Rules.StringNel](ls._1).run(ls._4) must beFalse
      }
  }
}
