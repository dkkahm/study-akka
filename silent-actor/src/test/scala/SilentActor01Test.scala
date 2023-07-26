import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestActorRef, TestKit}
import com.akkademy.StopSystemAfterAll
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class SilentActor01Test extends TestKit(ActorSystem("testsystem"))
  with AnyWordSpecLike
  with Matchers
  with StopSystemAfterAll {

  "A Silent Actor" must {
    "change state when it recives a message, single threaded" in {
      import SilentActor._

      val silentActor = TestActorRef[SilentActor]

      silentActor ! SilentMessage("whisper")
      silentActor.underlyingActor.state must contain("whisper")
    }

    "change state when it receives a message, multi-threaded" in {
      import SilentActor._

      val silentActor = system.actorOf(Props[SilentActor], "s3")
      silentActor ! SilentMessage("whisper1")
      silentActor ! SilentMessage("whisper2")
      silentActor ! GetState(testActor)
      expectMsg(Vector("whisper1", "whisper2"))
    }
  }

}
