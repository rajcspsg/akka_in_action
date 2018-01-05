package app

import actors.{BoxOfficeActor, RemoteLookupProxy}
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import rest.RestRoutes
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object FrontendMain extends App {

  val config = ConfigFactory.load("frontend")

  implicit val system = ActorSystem("frontend", config)

  implicit val ec = system.dispatcher

  val host = system.settings.config.getString("http.host")
  val port = system.settings.config.getInt("http.port")

  val api = new RestRoutes {

    override implicit def executionContext: ExecutionContext = ec

     def createPath(): String = {
      val config = ConfigFactory.load("frontend").getConfig("backend")
      val host = config.getString("host")
      val port = config.getInt("port")
      val protocol = config.getString("protocol")
      val systemName = config.getString("system")
      val actorName = config.getString("actor")
      s"$protocol://$systemName@$host:$port/$actorName"
    }

    override def createBoxOffice: ActorRef = {
      val path = createPath()
      system.actorOf(Props(new RemoteLookupProxy(path)), "lookupBoxOffice")
    }

    override implicit def requestTimeout: Timeout = FiniteDuration(5, scala.concurrent.duration.SECONDS)
  }

  implicit val materializer = ActorMaterializer()
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(api.routes, host, port) //Starts the HTTP server

  val log = Logging(system.eventStream, "go-ticks")
  bindingFuture.map { serverBinding =>
    log.info(s"RestApi bound to ${serverBinding.localAddress} ")
  }

}
