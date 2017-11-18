package com.michalplachta.freeprisoners.algebras

import cats.:<:
import cats.free.Free
import com.michalplachta.freeprisoners.PrisonersDilemma.{Decision, Prisoner}

object GameOps {
  sealed trait Game[A]
  final case class SendDecision(player: Prisoner,
                                opponent: Prisoner,
                                decision: Decision)
      extends Game[Unit]
  final case class ClearPlayerDecisions(player: Prisoner) extends Game[Unit]
  final case class GetOpponentDecision(player: Prisoner, opponent: Prisoner)
      extends Game[Option[Decision]]

  object Game {
    class Ops[S[_]](implicit s: Game :<: S) {
      def sendDecision(player: Prisoner,
                       opponent: Prisoner,
                       decision: Decision): Free[S, Unit] =
        Free.liftF(s.inj(SendDecision(player, opponent, decision)))

      def clearPlayerDecisions(player: Prisoner): Free[S, Unit] =
        Free.liftF(s.inj(ClearPlayerDecisions(player)))

      def getOpponentDecision(player: Prisoner,
                              opponent: Prisoner): Free[S, Option[Decision]] =
        Free.liftF(s.inj(GetOpponentDecision(player, opponent)))
    }

    object Ops {
      def apply[S[_]](implicit s: Game :<: S): Ops[S] = new Ops[S]
    }
  }
}
