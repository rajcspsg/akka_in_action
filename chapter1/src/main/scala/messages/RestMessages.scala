package messages

import spray.json.DefaultJsonProtocol

case class EventDescription(tickets: Int) {
  require(tickets > 0)
}

case class TicketRequest(tickets: Int) {
  require(tickets > 0)
}

case class Error(message: String)

trait EventMarshalling extends DefaultJsonProtocol {
  import BoxOfficeMessages._

  implicit val eventDescriptionFormat = jsonFormat1(EventDescription)

  implicit val eventFormat = jsonFormat2(Event)

  implicit val eventsFormat = jsonFormat1(Events)

  implicit val ticketRequestFormat = jsonFormat1(TicketRequest)

  implicit val ticketFormat = jsonFormat1(TicketSellerMessages.Ticket)

  implicit val ticketsFormt = jsonFormat2(TicketSellerMessages.Tickets)

  implicit val errorFormat = jsonFormat1(Error)
}
