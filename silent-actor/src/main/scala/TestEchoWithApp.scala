import akka.actor.{Actor, ActorSystem, Props}

class EchoAppActor extends Actor {
  override def receive: Receive = {
    case msg => {
      val x = sender()
      println(x)
      x ! msg
    }
  }
}
object TestEchoWithApp extends App {
  val system = ActorSystem("SimpleSystem")
  val actor = system.actorOf(Props[EchoAppActor], "EchoActor")

  actor ! "some message"

  system.terminate()
}
