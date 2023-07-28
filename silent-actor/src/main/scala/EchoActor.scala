import akka.actor.Actor

private class EchoActor extends Actor {
  override def receive: Receive = {
    case msg => {
      val x = sender()
      println(x)
      x ! msg
    }
  }
}
