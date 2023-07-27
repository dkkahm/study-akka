package com.akkademy.pong

import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import com.akkademy.StopSystemAfterAll
import org.scalatest.funspec.AnyFunSpecLike
import org.scalatest.matchers.should.Matchers
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Success, Failure}

class PongActorTest extends TestKit(ActorSystem("testsystem"))
  with AnyFunSpecLike
  with Matchers
  with StopSystemAfterAll {

  implicit val timeout = Timeout(5.seconds)

  val pongActor = system.actorOf(Props(classOf[PongActor]))

  def askPong(msg: String): Future[String] = {
    (pongActor ? msg).mapTo[String]
  }

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

    it("chain furture") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val r: Future[String] = askPong("Ping").flatMap(x => askPong(x))
      r.onComplete {
        case Success(v) => println(s"Success: ${v}")
        case Failure(ex) => println(s"Failure: ${ex}")
      }
    }

    it("cause error") {
      import scala.concurrent.ExecutionContext.Implicits.global

      askPong("causeError").onComplete {
        case Failure(ex) => println("Got exception")
      }
    }

    it("recover") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val f = askPong("causeError").recover {
        case t: Exception => "default"
      }

      val result = Await.result(f, 1.second)
      result should equal("default")
    }

    it("recoverWith") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val f = askPong("causeError").recoverWith{
        case t: Exception => askPong("Ping")
      }

      val result = Await.result(f, 1.second)
      result should equal("Pong")
    }

    it("success then fail then recover") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val f = askPong("Ping").
        flatMap(x => askPong("Ping" + x)).
        recover({ case _: Exception => "error"})

      val result = Await.result(f, 1.second)
      result should equal("error")
    }

    it("fail then recover") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val f = askPong("causeError").
        flatMap(x => askPong("Ping")).
        recover({ case _: Exception => "recover" })

      val result = Await.result(f, 1.second)
      result should equal("recover")
    }

    it("fail then recoverWith") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val f = askPong("causeError").
        flatMap(x => askPong("Ping")).
        recoverWith({ case _: Exception => askPong("Ping") })

      val result = Await.result(f, 1.second)
      result should equal("Pong")
    }

    it("combine future") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val f1 = Future {4}
      val f2 = Future {5}

      val fc = for (
        res1 <- f1;
        res2 <- f2
      ) yield res1 + res2

      val r = Await.result(fc, 1.second)
      r should equal(9)
    }


    it("future of list using sequence") {
      import scala.concurrent.ExecutionContext.Implicits.global

      val listOfFutures: List[Future[String]] = List("Pong", "Pong", "failed").map(askPong)
      val futureOfList: Future[List[String]] = Future.sequence(listOfFutures.map(future =>
        future.recover { case _: Exception => "" }
      ))

    }
  }


}
