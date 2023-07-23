import akka.actor.SupervisorStrategy.{Restart, Resume}
import akka.actor.{Actor, ActorSystem, OneForOneStrategy, Props}

object SupervisorExample extends App {
  case object CreateChild

  case class SignalChildren(order: Int)

  case class PrintSignal(order: Int)
  case class DivideNumber(n: Int, d: Int)
  case object BadStuff

  class ParentActor extends Actor {
    private var number = 0

    override def receive: Receive = {
      case CreateChild =>
        context.actorOf(Props[ChildActor], "child" + number)
        number += 1
      case SignalChildren(n) =>
        context.children.foreach(_ ! PrintSignal(n))
    }

    override val supervisorStrategy = OneForOneStrategy(loggingEnabled = false) {
      case ae: ArithmeticException => Resume
      case _: Exception => Restart
    }
  }

  class ChildActor extends Actor {
    println("Child created")
    override def receive: Receive = {
      case PrintSignal(n) => println(n + " " + self)
      case DivideNumber(n, d) => println(n / d)
      case BadStuff => throw new RuntimeException("Stuff happened")
    }

    override def preStart(): Unit = {
      super.preStart()
      println("preStart")
    }

    override def postStop(): Unit = {
      super.postStop()
      println("postStop")
    }

    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      super.preRestart(reason, message)
      println("preRestart")
    }

    override def postRestart(reason: Throwable): Unit = {
      super.postRestart(reason)
      println("postRestart")
    }
  }

  val system = ActorSystem("SupervisorExample")
  val actor = system.actorOf(Props[ParentActor], "Parent1")

  actor ! CreateChild

  val child0 = system.actorSelection("/user/Parent1/child0")
  child0 ! DivideNumber(4, 0)
  child0 ! DivideNumber(4, 2)
  child0 ! BadStuff

  Thread.sleep(1000)
  system.terminate()
}
