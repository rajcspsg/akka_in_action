import actors.SimpleActor
import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object BackendMain extends App {
  val config = ConfigFactory.load("backend")
  val backend = ActorSystem("backend", config)
  val backendRef = backend.actorOf(Props[SimpleActor], "simple")
  backendRef ! "msg"
}
