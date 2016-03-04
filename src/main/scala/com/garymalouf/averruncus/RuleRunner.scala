package com.garymalouf.averruncus

import scala.language.higherKinds
import scalaz.{ \/, Applicative, Validation }
import scalaz.syntax.apply._
import scalaz.syntax.std.boolean._
import scalaz.syntax.std.option._

object runners {

  implicit def toOption[E](b: Boolean, e: E): Option[Unit] =
    b.option(())

  implicit def toDisjunction[E](b: Boolean, e: E): \/[E, Unit] =
    toOption(b, e) \/> e

  implicit def toValidation[E](b: Boolean, e: E): Validation[E, Unit] =
    toOption(b, e).toSuccess(e)
}

object RuleRunner {

  type Runner[I, E, G[_]] = (I, E) => G[Unit]

  def run[I, E, G[_]: Applicative](rules: Rule[I, E]*)(in: I)(
    implicit
    r: Runner[Boolean, E, G]
  ): G[Unit] =
    rules.map(Rule.run(_)(in)).reduce(_ *> _)
}
