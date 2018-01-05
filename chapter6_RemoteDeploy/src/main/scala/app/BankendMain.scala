package app

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory

object BankendMain extends App {

  val configBackend = ConfigFactory.load("backend")
  val systemBackend = ActorSystem("backend", configBackend)

}
