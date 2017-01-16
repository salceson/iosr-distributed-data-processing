package controllers

import com.google.inject.Inject
import controllers.Application._
import iosr.models.WeekOfYear
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import services.DatabaseService
import play.api.i18n.{I18nSupport, MessagesApi}

import scala.concurrent.Future

class Application @Inject()(databaseService: DatabaseService,
                            val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action {
    Ok(views.html.index())
  }

  val durationForm = Form(
    mapping(
      "yearFrom" -> number(2005, 2015),
      "weekFrom" -> number(1, 53),
      "yearTo" -> number(2005, 2015),
      "weekTo" -> number(1, 53)
    )(DurationRequest.apply)(DurationRequest.unapply)
  )

  val durationFromToForm = Form(
    mapping(
      "yearFrom" -> number(2005, 2015),
      "weekFrom" -> number(1, 53),
      "yearTo" -> number(2005, 2015),
      "weekTo" -> number(1, 53),
      "from" -> nonEmptyText,
      "to" -> nonEmptyText
    )(DurationFromToRequest.apply)(DurationFromToRequest.unapply)
  )

  val durationAirportForm = Form(
    mapping(
      "yearFrom" -> number(2005, 2015),
      "weekFrom" -> number(1, 53),
      "yearTo" -> number(2005, 2015),
      "weekTo" -> number(1, 53),
      "airport" -> nonEmptyText
    )(DurationAirportRequest.apply)(DurationAirportRequest.unapply)
  )

  def weekdaysPerformanceForm = Action {
    Ok(views.html.weekdaysform(durationForm))
  }

  def weekdaysPerformance = Action.async { implicit request =>
    durationForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.weekdaysform(formWithErrors))),
      form => {
        val weekOfYearFrom = WeekOfYear(form.weekFrom, form.yearFrom)
        val weekOfYearTo = WeekOfYear(form.weekTo, form.yearTo)
        databaseService.weekdaysArrivalDelay(weekOfYearFrom, weekOfYearTo) map { result =>
          Ok(views.html.weekdaysresult(result))
        }
      }
    )
  }

  def airportsPopularityForm = Action {
    Ok(views.html.airportspopularityform(durationForm))
  }

  def airportsPopularity = Action.async { implicit request =>
    durationForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.airportspopularityform(formWithErrors))),
      form => {
        val weekOfYearFrom = WeekOfYear(form.weekFrom, form.yearFrom)
        val weekOfYearTo = WeekOfYear(form.weekTo, form.yearTo)
        databaseService.mostPopularAirports(weekOfYearFrom, weekOfYearTo) map { result =>
          Ok(views.html.airportspopularityresult(result))
        }
      }
    )
  }

  def airportTopCarriersForm = Action {
    Ok(views.html.airporttopcarriersform(durationAirportForm))
  }

  def airportTopCarriers = Action.async { implicit request =>
    durationAirportForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.airporttopcarriersform(formWithErrors))),
      form => {
        val weekOfYearFrom = WeekOfYear(form.weekFrom, form.yearFrom)
        val weekOfYearTo = WeekOfYear(form.weekTo, form.yearTo)
        databaseService.airportDepartureDelay(weekOfYearFrom, weekOfYearTo, form.airport) map { result =>
          Ok(views.html.airporttopcarriersresult(result))
        }
      }
    )
  }

  def routeMeanForm = Action {
    Ok(views.html.routemeandelayform(durationFromToForm))
  }

  def routeMean = Action.async { implicit request =>
    durationFromToForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(views.html.routemeandelayform(formWithErrors))),
      form => {
        val weekOfYearFrom = WeekOfYear(form.weekFrom, form.yearFrom)
        val weekOfYearTo = WeekOfYear(form.weekTo, form.yearTo)
        databaseService.routeMeanArrivalDelay(weekOfYearFrom, weekOfYearTo, form.from, form.to) map { result =>
          Ok(views.html.routemeandelayresult(form.from, form.to, result))
        }
      }
    )
  }
}

object Application {

  case class DurationRequest(yearFrom: Int, weekFrom: Int, yearTo: Int, weekTo: Int)

  case class DurationFromToRequest(yearFrom: Int, weekFrom: Int, yearTo: Int, weekTo: Int,
                                   from: String, to: String)

  case class DurationAirportRequest(yearFrom: Int, weekFrom: Int, yearTo: Int, weekTo: Int,
                                    airport: String)

}
