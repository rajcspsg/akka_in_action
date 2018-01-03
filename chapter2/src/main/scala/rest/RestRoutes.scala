package rest
import actors.BoxOffice
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import messages.BoxOfficeMessages.{EventCreated, EventExists}
import messages.{Error, EventDescription, EventMarshalling}

trait RestRoutes extends BoxOfficeApi with EventMarshalling {

  import StatusCodes._

  def routes: Route = eventRoute ~ eventsRoute ~ ticketsRoute

  def eventsRoute = pathPrefix("events") {
    pathEndOrSingleSlash {
      get {
        onSuccess(getEvents) { events =>
          complete(OK, events)
        }
      }
    }
  }

  def eventRoute = pathPrefix("events" / Segment) { event =>
    pathEndOrSingleSlash {

      post {
        entity(as[EventDescription]) { ed =>
          onSuccess(createEvent(event, ed.tickets)) {
            case EventCreated(event) => complete(Created, event)
            case EventExists =>
              val err = Error(s"$event event exists already")
              complete(BadRequest, err)
          }
        }
      } ~
      get {
        onSuccess(getEvent(event)) {
          _.fold(complete(NotFound))(e => complete(OK, e))
        }
      } ~
      delete {
        onSuccess(cancelEvent(event)) {
          _.fold(complete(NotFound))(e => complete(OK, e))
        }
      }
    }
  }

  /*def ticketsRoute = pathPrefix("events" / Segment / "tickets") { event =>
    post {
      pathEndOrSingleSlash {
        entity(as[messages.TicketRequest]) { request =>
          onSuccess(requestTickets(event, request.tickets)) { tickets =>
            if(tickets.entries.isEmpty)
              complete(NotFound)
            else
              complete(Created, tickets)
          }
        }
      }
    }
  }*/

  def ticketsRoute =
    pathPrefix("events" / Segment / "tickets") { event =>
      post {
        pathEndOrSingleSlash {
          // POST /events/:event/tickets
          entity(as[messages.TicketRequest]) { request =>
            onSuccess(requestTickets(event, request.tickets)) { tickets =>
              if(tickets.entries.isEmpty){
                //println(s"request is $request\n event is $event")
                complete(NotFound)
              }
              else complete(Created, tickets)
            }
          }
        }
      }
    }

}
