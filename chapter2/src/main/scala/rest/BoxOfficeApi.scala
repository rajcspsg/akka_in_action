package rest

import akka.actor.ActorRef
import akka.util.Timeout
import akka.pattern.ask
import messages.BoxOfficeMessages
import messages.TicketSellerMessages
import scala.concurrent.ExecutionContext


trait BoxOfficeApi  {

  def createBoxOffice: ActorRef

  implicit def executionContext: ExecutionContext
  implicit def requestTimeout : Timeout

  lazy val boxOffice = createBoxOffice

  def createEvent(event: String, nrOfTickets: Int) =
    boxOffice.ask(BoxOfficeMessages.CreateEvent(event, nrOfTickets)).mapTo[BoxOfficeMessages.EventResponse]

  def getEvents = boxOffice.ask(BoxOfficeMessages.GetEvents).mapTo[BoxOfficeMessages.Events]

  def getEvent(event: String) = boxOffice.ask(BoxOfficeMessages.GetEvent(event)).mapTo[Option[BoxOfficeMessages.Event]]

  def cancelEvent(event: String) = boxOffice.ask(BoxOfficeMessages.CancelEvent(event)).mapTo[Option[BoxOfficeMessages.Event]]

  def requestTickets(event: String, tickets: Int) =
    boxOffice.ask(BoxOfficeMessages.GetTickets(event, tickets)).mapTo[TicketSellerMessages.Tickets]

}
