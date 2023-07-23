import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success
import scala.util.Failure

object AskPattern extends App {
  case object AskName
  case class NameResponse(name: String)
  case class AskNameOf(other: ActorRef)

  implicit val timeout = Timeout(1.seconds)

  class AskActor(val name: String) extends Actor {
    implicit val ec = context.system.dispatcher

    override def receive: Receive = {
      case AskName =>
        Thread.sleep(1000)
        sender ! NameResponse(name)
      case AskNameOf(other) =>
        println("AskNameOf..")
        val f = other ? AskName
        f.onComplete {
          case Success(NameResponse(n)) =>
            println("They said their name was " + n)
          case Success(s) =>
            println("They didn't tell us their name")
          case Failure(ex) =>
            println("Asking their name failed.")
        }
    }
  }

  val system = ActorSystem("SimpleSystem")
  val actor = system.actorOf(Props(new AskActor("Kam")), "AskActor1")
  val actor2 = system.actorOf(Props(new AskActor("Kim")), "AskActor2")
  implicit val ec = system.dispatcher

  val answer = actor ? AskName

  answer.foreach(n => println("Name is " + n))

  actor ! AskNameOf(actor2)

  Thread.sleep(1000)

  system.terminate()
}
