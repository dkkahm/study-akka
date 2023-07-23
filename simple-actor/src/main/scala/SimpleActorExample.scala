import akka.actor.{Actor, ActorSystem, Props}

object SimpleActorExample extends App {
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case s: String => println("String: " + s)
      case i: Int => println("Number: " + i)
    }

    def foo = println("Normal method")
  }

  val system = ActorSystem("SimpleSystem")
  val actor = system.actorOf(Props[SimpleActor], "SimpleActor")

  println("Befor messages");
  actor ! "Hi there"
  println("After String");
  actor ! 123
  println("After Int");
  actor ! 'a'
  println("After Char");

  system.terminate()
}
