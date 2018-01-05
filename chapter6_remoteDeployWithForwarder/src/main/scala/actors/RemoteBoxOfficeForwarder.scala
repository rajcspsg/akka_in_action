package actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props, ReceiveTimeout, Terminated}
import akka.util.Timeout

import scala.concurrent.duration._

class RemoteBoxOfficeForwarder(implicit  timeout: Timeout) extends Actor with ActorLogging {

  context.setReceiveTimeout(3 seconds)

  deployAndWatch()

  def deployAndWatch(): Unit = {
    val actor = context.actorOf(BoxOfficeActor.props, BoxOfficeActor.name)
    context.watch(actor)
    log.info(s"switching to be maybe Active State")
    context.become(maybeActive(actor))
    context.setReceiveTimeout(Duration.Undefined)
  }

  override def receive: Receive = deploying

  def deploying: Receive = {
    case ReceiveTimeout =>
      deployAndWatch()

    case msg: Any =>
      log.error(s"Ignoring message $msg, remote actor is not ready yet")
  }

  def maybeActive(actor: ActorRef): Receive = {

    case Terminated(actorRef) =>
      log.info(s"Actor $actorRef is terminated")
      log.info(s"switching deploying state")
      context.become(deploying)
      context.setReceiveTimeout(3 seconds)
      deployAndWatch()

    case msg: Any =>
      actor forward(msg)
  }
}

object RemoteBoxOfficeForwarder {

  def props(implicit timeout: Timeout): Props = Props(new RemoteBoxOfficeForwarder())

  val name = "forwarder"
}
