package app

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object BankendMainDeployMain extends App {

  val configBackend = ConfigFactory.load("backend")
  val systemBackend = ActorSystem("backend", configBackend)

}
