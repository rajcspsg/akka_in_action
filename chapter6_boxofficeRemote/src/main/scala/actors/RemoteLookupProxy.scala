package actors

import akka.actor.{Actor, ActorIdentity, ActorLogging, ActorRef, Identify, ReceiveTimeout, Terminated}

import scala.concurrent.duration._

class RemoteLookupProxy(path: String) extends Actor with ActorLogging {

  context.setReceiveTimeout(3 seconds)
  sendIdentifyReq()

  def sendIdentifyReq(): Unit = {
    val selection = context.actorSelection(path)
    selection ! Identify(path)
  }


  override def receive: Receive = identify

  def identify: Receive = {
    case ActorIdentity(`path`, Some(actor)) =>
      context.setReceiveTimeout(Duration.Undefined)
      log.info(s"switching to active state")
      context.become(active(actor))
      context.watch(actor)

    case ActorIdentity(`path`, None) =>
      log.error(s"remote actor at path $path is not available ")

    case ReceiveTimeout =>
      sendIdentifyReq()

    case msg: Any =>
      log.error(s"Ignoring message $msg, since path $path is not ready yet")
  }

  def active(actor: ActorRef): Receive = {
    case Terminated(actorRef) =>
      log.info(s"Actor $actorRef terminated")
      context.become(identify)
      log.info(s"switching to identify state")
      context.setReceiveTimeout(3 seconds)
      sendIdentifyReq()

    case msg: Any => actor forward(msg)
  }

}
