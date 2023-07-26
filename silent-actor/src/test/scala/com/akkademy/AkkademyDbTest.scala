package com.akkademy

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
import akka.util.Timeout
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import com.akkademy.message.SetRequest
class AkkademyDbTest extends TestKit(ActorSystem("testsystem"))
  with AnyFunSpecLike
  with Matchers
  with StopSystemAfterAll {
  implicit val timeout = Timeout(5.seconds)

  describe("akkademyDb") {
    describe("given SetRequest") {
      it("should place key/value into map") {
        val actorRef = TestActorRef[AkkademyDb]
        actorRef ! SetRequest("key", "value")

        val akkademyDb = actorRef.underlyingActor
        akkademyDb.map.get("key") should equal(Some("value"))
      }
    }
  }

}
