package custom.eventbus

import akka.event._
import messages.Order

class OrderMessageBus extends EventBus with LookupClassification with ActorEventBus {
  type Event = Order
  type Classifier = Boolean

  val mapSize = 2

  protected def classify(event: OrderMessageBus#Event): Boolean = event.number > 1

  protected def publish(event: OrderMessageBus#Event, subscriber: OrderMessageBus#Subscriber): Unit = {
    subscriber ! event
  }

}