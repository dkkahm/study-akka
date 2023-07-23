import akka.actor.{Actor, ActorSystem, Props}
import scala.concurrent.duration._

object SchedulerExample extends App {
  case object Count

  class SchedulerActor extends Actor {
    var n = 0
    override def receive: Receive = {
      case Count =>
        n += 1
        println(n)
    }
  }

  val system = ActorSystem("SimpleSystem")
  val actor = system.actorOf(Props[SchedulerActor], "SchedulerActor")
  implicit val ec = system.dispatcher

  actor ! Count

  system.scheduler.scheduleOnce(1.second)(actor ! Count)

  val can = system.scheduler.scheduleWithFixedDelay(0.second, 100.milli, actor, Count)

  Thread.sleep(2000)
  can.cancel

  system.terminate()
}
