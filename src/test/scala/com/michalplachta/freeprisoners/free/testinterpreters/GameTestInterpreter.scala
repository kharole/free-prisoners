package com.michalplachta.freeprisoners.free.testinterpreters

import cats.data.State
import cats.~>
import com.michalplachta.freeprisoners.free.algebras.GameOps.{
  ClearRegisteredDecision,
  Game,
  GetRegisteredDecision,
  RegisterDecision
}
import com.michalplachta.freeprisoners.states.GameState.GameStateA

class GameTestInterpreter extends (Game ~> GameStateA) {
  /*_*/
  def apply[A](game: Game[A]): GameStateA[A] = game match {
    case RegisterDecision(prisoner, decision) =>
      State { state =>
        (state.copy(decisions = state.decisions + (prisoner -> decision)), ())
      }

    case GetRegisteredDecision(prisoner) =>
      State { state =>
        if (state.delayInCalls <= 0) {
          (state, state.decisions.get(prisoner))
        } else {
          (state.copy(delayInCalls = state.delayInCalls - 1), None)
        }
      }

    case ClearRegisteredDecision(prisoner) =>
      State { state =>
        (state.copy(decisions = state.decisions - prisoner), ())
      }
  }
}
