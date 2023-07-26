package com.akkademy.pong

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import com.akkademy.StopSystemAfterAll
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

class PongActorTest extends TestKit(ActorSystem("testsystem"))
  with AnyFunSpecLike
  with Matchers
  with StopSystemAfterAll {

  implicit val timeout = Timeout(5.seconds)

  val pongActor = system.actorOf(Props(classOf[PongActor]))

  describe("Pong Actor") {
    it("should response with Pong") {
      val future = pongActor ? "Ping"
      val result = Await.result(future.mapTo[String], 1.seconds)
      result should equal("Pong")
    }

    it("should fail on unknown message") {
      val future = pongActor ? "unknown"
      intercept[Exception] {
        Await.result(future.mapTo[String], 1.second)
      }
    }
  }


}
