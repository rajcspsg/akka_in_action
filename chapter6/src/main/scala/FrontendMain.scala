

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object FrontendMain extends App {
  val config = ConfigFactory.load("frontend")
  val system = ActorSystem("frontend", config)
  val path = "akka.tcp://backend@localhost:2551/user/simple"
  val simpleActorRef = system.actorSelection(path)
  simpleActorRef ! "HelloRemoteWorld"
}
