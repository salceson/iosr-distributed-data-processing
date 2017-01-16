package services

import com.google.inject.{Inject, Singleton}
import iosr.models.WeekOfYear
import play.api.db.Database
import org.joda.time.DateTimeConstants._

import scala.collection.mutable
import scala.concurrent.Future

@Singleton
class DatabaseService @Inject()(db: Database) {

  import play.api.libs.concurrent.Execution.Implicits.defaultContext

  private val WeekDaysMap = Map(
    MONDAY -> "Monday",
    TUESDAY -> "Tuesday",
    WEDNESDAY -> "Wednesday",
    THURSDAY -> "Thursday",
    FRIDAY -> "Friday",
    SATURDAY -> "Saturday",
    SUNDAY -> "Sunday"
  )

  def weekdaysArrivalDelay(from: WeekOfYear,
                           to: WeekOfYear): Future[List[(String, Double)]] = Future {
    val connection = db.getConnection()
    try {
      val statement = connection.prepareStatement(
        "SELECT day_of_week, SUM(arrival_sum)/SUM(num) AS performance FROM weekdays " +
          "WHERE (year = ? AND week >= ?) OR (year > ? AND year < ?) OR (year = ? AND week <=?) " +
          "GROUP BY day_of_week ORDER BY performance ASC"
      )
      statement.setInt(1, from.year)
      statement.setInt(2, from.week)
      statement.setInt(3, from.year)
      statement.setInt(4, to.year)
      statement.setInt(5, to.year)
      statement.setInt(6, to.week)
      val resultSet = statement.executeQuery()
      val result = mutable.MutableList[(String, Double)]()
      while (resultSet.next()) {
        result += WeekDaysMap(resultSet.getInt(1)) -> resultSet.getDouble(2)
      }
      result.toList
    } finally {
      connection.close()
    }
  }

  def mostPopularAirports(from: WeekOfYear,
                          to: WeekOfYear): Future[List[(String, Long)]] = Future {
    val connection = db.getConnection()
    try {
      val statement = connection.prepareStatement(
        "SELECT airport, SUM(num) AS popularity FROM airports " +
          "WHERE (year = ? AND week >= ?) OR (year > ? AND year < ?) OR (year = ? AND week <=?) " +
          "GROUP BY airport ORDER BY popularity DESC LIMIT 5"
      )
      statement.setInt(1, from.year)
      statement.setInt(2, from.week)
      statement.setInt(3, from.year)
      statement.setInt(4, to.year)
      statement.setInt(5, to.year)
      statement.setInt(6, to.week)
      val resultSet = statement.executeQuery()
      val result = mutable.MutableList[(String, Long)]()
      while (resultSet.next()) {
        result += resultSet.getString(1) -> resultSet.getInt(2)
      }
      result.toList
    } finally {
      connection.close()
    }
  }

  def airportDepartureDelay(from: WeekOfYear,
                            to: WeekOfYear,
                            airport: String): Future[List[(String, Double)]] = Future {
    val connection = db.getConnection()
    try {
      val statement = connection.prepareStatement(
        "SELECT carrier, SUM(departure_sum)/SUM(num) AS performance FROM carriers " +
          "WHERE ((year = ? AND week >= ?) OR (year > ? AND year < ?) OR (year = ? AND week <=?)) " +
          "AND \"from\" = ? GROUP BY carrier ORDER BY performance ASC LIMIT 5"
      )
      statement.setInt(1, from.year)
      statement.setInt(2, from.week)
      statement.setInt(3, from.year)
      statement.setInt(4, to.year)
      statement.setInt(5, to.year)
      statement.setInt(6, to.week)
      statement.setString(7, airport)
      val resultSet = statement.executeQuery()
      val result = mutable.MutableList[(String, Double)]()
      while (resultSet.next()) {
        result += resultSet.getString(1) -> resultSet.getDouble(2)
      }
      result.toList
    } finally {
      connection.close()
    }
  }

  def routeMeanArrivalDelay(from: WeekOfYear,
                            to: WeekOfYear,
                            source: String,
                            destination: String): Future[Double] = Future {
    val connection = db.getConnection()
    try {
      val statement = connection.prepareStatement(
        "SELECT SUM(arrival_sum)/SUM(num) AS performance FROM carriers " +
          "WHERE ((year = ? AND week >= ?) OR (year > ? AND year < ?) OR (year = ? AND week <=?)) " +
          "AND \"from\" = ? AND \"to\" = ? LIMIT 1"
      )
      statement.setInt(1, from.year)
      statement.setInt(2, from.week)
      statement.setInt(3, from.year)
      statement.setInt(4, to.year)
      statement.setInt(5, to.year)
      statement.setInt(6, to.week)
      statement.setString(7, source)
      statement.setString(8, destination)
      val resultSet = statement.executeQuery()
      resultSet.next()
      resultSet.getDouble(1)
    } finally {
      connection.close()
    }
  }
}
