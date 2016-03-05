package com.garymalouf.averruncus.password

import ErrorProviders._
import org.scalacheck.Gen
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop.forAll
import org.specs2.ScalaCheck
import org.specs2.mutable.Specification
import scalaz.syntax.id._

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

  def inject[A](s: String, i: Int, e: A): String =
    s.splitAt(i) |> { case (l, r) => l ++ e.toString ++ r }

  val stringWithIndex: Gen[(String, Int)] =
    for {
      s <- Gen.alphaStr.suchThat(_.nonEmpty)
      i <- Gen.choose(0, (s.length - 1))
    } yield (s, i)

  "The hasUpper rule" should {
    "return whether pw includes uppercase" >>
      forAll(stringWithIndex, Gen.alphaChar) { (si, c) =>
        Rules.hasUpper[Rules.StringNel].run(si._1.toLowerCase) must beFalse
        val wu = inject(si._1.toLowerCase, si._2, c.toString.toUpperCase)
        Rules.hasUpper[Rules.StringNel].run(wu) must beTrue
      }
  }

  "The hasLower rule" should {
    "return whether pw includes lowercase" >>
      forAll(stringWithIndex, Gen.alphaChar) { (si, c) =>
        Rules.hasLower[Rules.StringNel].run(si._1.toUpperCase) must beFalse
        val wl = inject(si._1.toUpperCase, si._2, c.toString.toLowerCase)
        Rules.hasLower[Rules.StringNel].run(wl) must beTrue
      }
  }

  "The hasNumeric rule" should {
    "return whether pw includes lowercase" >>
      forAll(stringWithIndex, Gen.posNum[Int]) { (si, n) =>
        Rules.hasNumeric[Rules.StringNel].run(si._1) must beFalse
        val wn = inject(si._1, si._2, n)
        Rules.hasNumeric[Rules.StringNel].run(wn) must beTrue
      }
  }

  val sym: Gen[Char] =
    Gen.oneOf(List((1 to 47), (58 to 64), (91 to 96), (123 to 10000))
      .flatMap(_.toList)).map(_.toChar)

  "The hasSymbol rule" should {
    "return whether pw includes non alpha-numeric characters" >>
      forAll(stringWithIndex, sym) { (si, s) =>
        Rules.hasSymbol[Rules.StringNel].run(si._1) must beFalse
        val ws = inject(si._1, si._2, s)
        Rules.hasSymbol[Rules.StringNel].run(ws) must beTrue
      }
  }

  "The noWhitespace rule" should {
    "return whether pw includes no whitespace" >>
      forAll(stringWithIndex, Gen.posNum[Int]) { (si, l) =>
        Rules.noWhitespace[Rules.StringNel].run(si._1) must beTrue
        val wws = inject(si._1, si._2, List.fill(l)(" ").mkString)
        Rules.noWhitespace[Rules.StringNel].run(wws) must beFalse
      }
  }
}
