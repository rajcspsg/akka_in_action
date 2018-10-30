import org.scalatest._
import akka.testkit.{TestKit, ImplicitSender, TestActors, TestProbe}
import akka.actor._
import scala.concurrent.duration._

trait StopSystemAfterAll extends BeforeAndAfterAll {
  this: TestKit with Suite =>

  override def afterAll(): Unit = {

  }
}

class PipeFilterTest extends TestKit(ActorSystem("testSystem")) with WordSpecLike with ImplicitSender with BeforeAndAfterAll {


  override def beforeAll(): Unit = {

  }
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "PipeFilter Actor" must {
    "send  messages if it passes the filter criteria" in {
      val endProbe = TestProbe()
      val speedFilterRef = system.actorOf(Props(new SpeedFilter(50, endProbe.ref)))
      val licenseFilterRef = system.actorOf(Props(new LicenseFilter(speedFilterRef)))
      val msg =  Photo("123xyz", 60)
      licenseFilterRef ! msg
      endProbe.expectMsg(msg)

      licenseFilterRef ! Photo("", 60)
      endProbe.expectNoMsg(1 second)

      licenseFilterRef ! Photo("123xyz", 49)
      endProbe.expectNoMsg(1 second)

    }
  }

}