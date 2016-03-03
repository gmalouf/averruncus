package com.garymalouf.averruncus

import scala.language.higherKinds
import scalaz.Applicative

object Rule {

  def run[I, E, G[_]: Applicative](rule: Rule[I, E])(in: I)(
    implicit
    r: RuleRunner.Runner[Boolean, E, G]
  ): G[Unit] =
    r(rule.run(in), rule.error)
}

case class Rule[I, E](error: E, run: I => Boolean)
