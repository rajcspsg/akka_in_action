package lifecycle.hooks

import akka.actor.{Actor, ActorLogging}
import akka.event.LoggingAdapter

class LifeCycleHooks extends Actor with ActorLogging {

  log.info(s"Constructor !!!")

  override def preStart(): Unit = {
    log.info(s"preStart")
  }

  override def postStop(): Unit = {
    log.info(s"postStop")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    log.info("preRestart")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    log.info("postRestart")
    super.postRestart(reason)
  }

  //override def log: LoggingAdapter = super.log

  override def receive: Receive = {
    case "restart" =>
      throw new IllegalStateException("force restart")
    case msg =>
      log.info(s"receive")
      sender() ! msg
  }
}
