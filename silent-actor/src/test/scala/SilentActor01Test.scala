import akka.actor.ActorSystem
import akka.testkit.{TestActorRef, TestKit}
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
      fail("not implemented yet")
    }
  }

}
