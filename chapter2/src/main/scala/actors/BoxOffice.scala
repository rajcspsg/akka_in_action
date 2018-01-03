package actors

import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout
import akka.pattern.{ask, pipe}
import messages.BoxOfficeMessages._
import messages.TicketSellerMessages
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BoxOffice {
  def props(implicit timeout: Timeout) = Props(new BoxOffice())

  def name = "boxOffice"

}

class BoxOffice(implicit  timeout: Timeout) extends Actor {

  import context._

  def createTicketSeller(name: String) = context.actorOf(TicketSeller.props(name), name)

  override def receive: Receive = {

    case CreateEvent(name, tickets) =>
      def create = {
        val eventTickets = createTicketSeller(name)
        val newTickets = (1 to tickets).map { ticketId =>
          TicketSellerMessages.Ticket(ticketId)
        }.toVector
        eventTickets ! TicketSellerMessages.Add(newTickets)
        sender() ! EventCreated(Event(name, tickets))
      }
      context.child(name).fold(create) (_ => EventExists)

    case GetTickets(event, tickets) =>
      def notFound = sender() ! TicketSellerMessages.Tickets(event)
      def buy(child: ActorRef) = child.forward(TicketSellerMessages.Buy(tickets))

      val actorRef:Option[ActorRef] = context.child(event)
      // println(s"actorRef is ${actorRef.isDefined}")
      actorRef.fold(notFound)(buy)

    case GetEvent(event) =>
      def notFound = sender() ! None
      def getEvent(child: ActorRef) = child ! child.forward(TicketSellerMessages.GetEvent)
      context.child(event).fold(notFound)(getEvent)

    case CancelEvent(event) =>
      def notFound = sender() ! None
      def cancelEvent(child: ActorRef) = child ! child.forward(TicketSellerMessages.Cancel)
      context.child(event).fold(notFound)(cancelEvent)

    case GetEvents =>
      import akka.pattern.{ask, pipe}
      def getEvents = context.children.map { child =>
        self.ask(GetEvent(child.path.name)).mapTo[Option[Event]]
      }

      def convertToEvents(f: Future[Iterable[Option[Event]]]) = f.map(_.flatten).map(l => Events(l.toVector))

      pipe(convertToEvents(Future.sequence(getEvents))) to sender()
  }
}
