package iosr.models

import com.github.nscala_time.time.Imports._

case class WeekOfYear(week: Int, year: Int)

object WeekOfYear {
  def fromDate(year: Int, month: Int, day: Int): WeekOfYear = {
    val date = new LocalDate(year, month, day)
    WeekOfYear((date.getDayOfYear - 1) / 7 + 1, year)
  }
}

case class WeekOfYearCarrierFromTo(weekOfYear: WeekOfYear,
                                   carrier: String,
                                   from: String,
                                   to: String)

case class Weekday(dayOfWeek: Int)

object Weekday {
  def fromDate(year: Int, month: Int, day: Int): Weekday = {
    val date = new LocalDate(year, month, day)
    Weekday(date.getDayOfWeek)
  }
}

case class WeekOfYearWeekday(weekOfYear: WeekOfYear, weekday: Weekday)

case class Airport(code: String)

case class WeekOfYearAirport(weekOfYear: WeekOfYear, airport: Airport)
