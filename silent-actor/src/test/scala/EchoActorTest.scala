import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import com.akkademy.StopSystemAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class EchoActorTest extends TestKit(ActorSystem("testsystem"))
  with AnyWordSpecLike
  with ImplicitSender
  with StopSystemAfterAll {

  "Reply with this same message it receives without ask" in {
    val echo = system.actorOf(Props[EchoActor], "echo2")
    echo ! "some message"
    expectMsg("some message")
  }
}
