package com.michalplachta.freeprisoners

import cats.data.EitherK
import cats.free.Free
import com.michalplachta.freeprisoners.algebras.BotOps.{Bot, Strategies}
import com.michalplachta.freeprisoners.algebras.PlayerOps.Player
import com.michalplachta.freeprisoners.interpreters.{
  BotInterpreter,
  PlayerConsoleInterpreter
}

object SinglePlayerGame extends App {
  type SinglePlayer[A] = EitherK[Player, Bot, A]

  def program(playerOps: Player.Ops[SinglePlayer],
              botOps: Bot.Ops[SinglePlayer]): Free[SinglePlayer, Unit] = {
    import playerOps._, botOps._
    for {
      playerPrisoner <- meetPrisoner("Welcome to Single Player Game")
      botPrisoner <- createBot("Romain", Strategies.alwaysBlame)
      playerDecision <- questionPrisoner(playerPrisoner, botPrisoner)
      botDecision <- getDecision(botPrisoner, playerPrisoner)
      _ <- displayVerdict(playerPrisoner,
                          PrisonersDilemma.verdict(playerDecision, botDecision))
      _ <- displayVerdict(botPrisoner,
                          PrisonersDilemma.verdict(botDecision, playerDecision))
    } yield ()
  }

  program(
    new Player.Ops[SinglePlayer],
    new Bot.Ops[SinglePlayer]
  ).foldMap(PlayerConsoleInterpreter or BotInterpreter)
}
