package com.garymalouf.averruncus

import scala.language.higherKinds
import scalaz.Applicative

object Rule {

  def run[E, I, G[_]: Applicative](rule: Rule[E, I])(in: I)(
    implicit
    r: RuleRunner.Runner[Boolean, E, G]
  ): G[Unit] =
    r(rule.run(in), rule.error)
}

case class Rule[E, I](error: E, run: I => Boolean)
