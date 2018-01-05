package actors

import akka.actor.Actor

class SimpleActor extends Actor {

  override def receive: Receive = {
    case msg => println(s" receive msg $msg by actor ${self.path}")
  }

}
