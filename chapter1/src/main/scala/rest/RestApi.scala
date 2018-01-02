package rest

import actors.BoxOffice
import akka.actor.{ActorSystem, Props}
import akka.util.Timeout

class RestApi(system: ActorSystem, timeout: Timeout) extends RestRoutes {
  implicit val requestTimeout = timeout
  implicit val executionContext = system.dispatcher

  def createBoxOffice = system.actorOf(BoxOffice.props, BoxOffice.name)
}
