package actors

import akka.actor.{Actor, Props, PoisonPill}
import messages.TicketSellerMessages._
import messages.BoxOfficeMessages

class TicketSeller(event: String) extends Actor {

var tickets = Vector.empty[Ticket]

  override def receive: Receive = {
    case Add(newTickets) => tickets = tickets ++ newTickets
    case Buy(nrOfTickets) =>
      val entries = tickets.take(nrOfTickets).toVector
      if(entries.size > nrOfTickets) {
        sender() ! Tickets(event, entries)
        tickets = tickets.drop(nrOfTickets)
      } else {
        sender() ! Tickets(event)
      }
    case GetEvent =>
        sender() ! Some(BoxOfficeMessages.Event(event, tickets.size))
    case Cancel =>
        sender() ! Some(BoxOfficeMessages.Event(event, tickets.size))
        self ! PoisonPill
  }
}

object TicketSeller {
  def props(event: String) = Props(new TicketSeller(event))
}
