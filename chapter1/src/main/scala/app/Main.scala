package app

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import com.typesafe.config.ConfigFactory
import rest.RestApi
import akka.stream.ActorMaterializer

import scala.concurrent.Future

object Main extends App with RequestTimeout {
  val config = ConfigFactory.load()
  val host = config.getString("http.host")
  val port = config.getInt("http.port")

  implicit val system = ActorSystem()
  implicit val ec = system.dispatcher

  val api = new RestApi(system, requestTimeout).routes

  implicit val materializer = ActorMaterializer()

  val bindingFuture: Future[ServerBinding] = Http().bindAndHandle(api, host, port)
}
