package app

import actors.{BoxOfficeActor, RemoteBoxOfficeForwarder}
import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import rest.RestRoutes

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration._

object FrontendMainDeployMain extends App {

  val config = ConfigFactory.load("frontend-remote-deploy")

  implicit val system = ActorSystem("frontend", config)

  implicit val ec = system.dispatcher

  val host = system.settings.config.getString("http.host")
  val port = system.settings.config.getInt("http.port")

  val api = new RestRoutes {

    override implicit def executionContext: ExecutionContext = ec

    override def createBoxOffice: ActorRef =
      system.actorOf(RemoteBoxOfficeForwarder.props(5 seconds), BoxOfficeActor.name)

    override implicit def requestTimeout: Timeout = FiniteDuration(5, scala.concurrent.duration.SECONDS)

  }

  implicit val materializer = ActorMaterializer()
  val bindingFuture: Future[ServerBinding] =
    Http().bindAndHandle(api.routes, host, port)

  val log = Logging(system.eventStream, "go-ticks")

  bindingFuture.map { serverBinding =>
    log.info(s"RestApi bound to ${serverBinding.localAddress} ")
  }

}
