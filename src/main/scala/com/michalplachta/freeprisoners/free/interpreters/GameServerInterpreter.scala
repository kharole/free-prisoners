package com.michalplachta.freeprisoners.free.interpreters

import java.util.concurrent.TimeUnit

import akka.actor.ActorSystem
import akka.util.Timeout
import cats.effect.IO
import cats.~>
import com.michalplachta.freeprisoners.actors.GameServer.{
  ClearSavedDecision,
  GetSavedDecision,
  SaveDecision
}
import com.michalplachta.freeprisoners.actors.ServerCommunication._
import com.michalplachta.freeprisoners.free.algebras.GameOps._
import com.typesafe.config.ConfigFactory

import scala.concurrent.ExecutionContext

class GameServerInterpreter extends (Game ~> IO) {
  private val system = ActorSystem("gameClient")
  private val config = ConfigFactory.load().getConfig("app.game")
  private val maxRetries = config.getInt("client.max-retries")
  private val retryTimeout = Timeout(
    config.getDuration("client.retry-timeout").toMillis,
    TimeUnit.MILLISECONDS)
  implicit val executionContext: ExecutionContext = system.dispatcher

  private val server =
    system.actorSelection(config.getString("server.path"))

  /*_*/
  def apply[A](game: Game[A]): IO[A] = game match {
    case RegisterDecision(prisoner, decision) =>
      tellServer(server, SaveDecision(prisoner, decision))
    case GetRegisteredDecision(prisoner) =>
      askServer(server, GetSavedDecision(prisoner), maxRetries, retryTimeout)
    case ClearRegisteredDecision(prisoner) =>
      tellServer(server, ClearSavedDecision(prisoner))
  }

  def terminate(): Unit = system.terminate()
}
