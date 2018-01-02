import akka.actor.{ActorSystem, Props}
import akka.testkit.TestKit
import lifecycle.hooks.LifeCycleHooks
import org.scalatest.{BeforeAndAfterAll, MustMatchers, WordSpecLike}

class LifeCycleTest extends TestKit(ActorSystem("test"))
  with WordSpecLike
  with MustMatchers
  with BeforeAndAfterAll {

  "LifeCyclehooks" must  {
    "hook test for actor" in {
      val testActorRef = system.actorOf(Props[LifeCycleHooks], "LifeCycleHooks")
      testActorRef ! "restart"
      testActorRef tell("msg", testActor)
      expectMsg("msg")
      system.stop(testActorRef)
      Thread.sleep(1000)
    }
  }

  override def afterAll(): Unit = {
    system.terminate()
    super.afterAll()
  }

}
