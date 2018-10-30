import org.scalatest._
import akka.testkit.{TestKit, ImplicitSender, TestActors, TestProbe}
import akka.actor.{ActorRef, Actor, Props, ActorSystem, PoisonPill, DeadLetter}
import scala.concurrent.duration._
import messages.Order
import custom.eventbus.OrderMessageBus
import akka.testkit.TestActors.EchoActor
import prop._

class EventStreamTest extends TestKit(ActorSystem("test")) with WordSpecLike  with Matchers with ImplicitSender with BeforeAndAfterAll {

  "EventStream in action" must {
      "should send messages to all actors that subscribe to it" in  {

        val deliverProbe = TestProbe()
        val giftProbe = TestProbe()

        system.eventStream.subscribe(deliverProbe.ref, classOf[Order])
        system.eventStream.subscribe(giftProbe.ref, classOf[Order])

        val msg = Order("me", "Akka in Action", 3)
        system.eventStream.publish(msg)

        deliverProbe.expectMsg(msg)
        giftProbe.expectMsg(msg)

        system.eventStream.unsubscribe(giftProbe.ref)

        system.eventStream.publish(msg)
        deliverProbe.expectMsg(msg)
        giftProbe.expectNoMsg(3 seconds)

      }
  }

  "Custom Event Stream" must {
    "send messages to subscribed actors" in {
      val bus = new custom.eventbus.OrderMessageBus

      val singleBookProbe = TestProbe()
      bus.subscribe(singleBookProbe.ref, false)
      val multiBooksProbe = TestProbe()
      bus.subscribe(multiBooksProbe.ref, true)

      val msg = Order("me", "Akka in Action", 1)
      bus.publish(msg)
      singleBookProbe.expectMsg(msg)
      multiBooksProbe.expectNoMsg(3 seconds)


      val msg2 = Order("me", "Akka in Action", 3)
      bus.publish(msg2)
      singleBookProbe.expectNoMsg(3 seconds)
      multiBooksProbe.expectMsg(msg2)
    }
  }

  "deadLetter Monitor " must {
    "catch messages that can be delivered" in {

      val deadLetterMonitorProbe = TestProbe()
      system.eventStream.subscribe(deadLetterMonitorProbe.ref, classOf[DeadLetter])

      val actor = system.actorOf(Props[EchoActor], "echo")
      actor ! PoisonPill

      val msg = Order("me", "Akka in Action", 1)
      actor ! msg
      val dead = deadLetterMonitorProbe.expectMsgType[DeadLetter]
      println(s"dead is $dead")
      dead.message shouldBe (msg)
      dead.sender shouldBe (testActor)
      dead.recipient shouldBe (actor)
    }
  }

  "sending DeadLetter Messages" must {
    "not modify the Sender information" in  {
      val deadLetterMonitorProbe = TestProbe()
      val actor = system.actorOf(Props[EchoActor], "echo")

      system.eventStream.subscribe(deadLetterMonitorProbe.ref, classOf[DeadLetter])

      val msg = Order("me", "Akka in Action", 3)
      val dead = DeadLetter(msg, testActor, actor)
      system.deadLetters ! dead
      deadLetterMonitorProbe.expectMsg(dead)
      system.stop(actor)
    }
  }

}