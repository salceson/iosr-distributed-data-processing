package controllers

import com.google.inject.Inject
import play.api.mvc._
import services.DatabaseService

class Application @Inject() (databaseService: DatabaseService) extends Controller {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def index = Action {
    Ok(views.html.index())
  }

  def departurePerformanceForAirports = Action.async {
    databaseService.simpleQuery().map(result => Ok(result.toString))
  }
}
