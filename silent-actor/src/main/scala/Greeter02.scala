import akka.actor.{Actor, ActorLogging, ActorRef, Props}

object Greeter02 {
  def props(listeners: Option[ActorRef] = None) = Props(new Greeter02(listeners))
}

class Greeter02(listeners: Option[ActorRef]) extends Actor with ActorLogging {
  override def receive: Receive = {
    case Greeting(who) =>
      val message = "Hello " + who + "!"
      log.info(message)
      listeners.foreach(_ ! message)
  }
}
