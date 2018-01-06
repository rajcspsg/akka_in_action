package app

import akka.actor._
import akka.cluster._
import com.typesafe.config._

object Main extends App {

  val seedConfig = ConfigFactory.load("seed")
  val ssedSystem = ActorSystem("words", seedConfig)

}
