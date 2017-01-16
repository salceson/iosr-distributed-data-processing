package services

import com.google.inject.{ Inject, Singleton }
import play.api.db.Database

import scala.concurrent.Future

@Singleton
class DatabaseService @Inject()(db: Database) {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  def simpleQuery(): Future[Long] = Future {
    val connection = db.getConnection()
    try {
      val statement = connection.createStatement
      val resultSet = statement.executeQuery(s"SELECT COUNT(*) AS flightsnum FROM flights")
      resultSet.next()
      resultSet.getLong(1)
    } finally {
      connection.close()
    }
  }
}
