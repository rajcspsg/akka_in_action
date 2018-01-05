package app

import actors.BoxOfficeActor
import akka.actor.ActorSystem
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import scala.concurrent.duration._

object BankendMain extends App {

  val configBackend = ConfigFactory.load("backend")
  val systemBackend = ActorSystem("backend", configBackend)
  implicit val requestTimeout = Timeout(5 seconds)
  val backendActor = systemBackend.actorOf(BoxOfficeActor.props, BoxOfficeActor.name)

}
