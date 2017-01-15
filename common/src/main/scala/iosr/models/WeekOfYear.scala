package iosr.models

case class WeekOfYear(week: Int, year: Int)

object WeekOfYear {
  implicit val weekOfYearOrdering = new Ordering[WeekOfYear] {
    override def compare(x: WeekOfYear, y: WeekOfYear): Int = {
      x.year.compare(y.year) match {
        case 0 => x.week.compare(y.week)
        case result => result
      }
    }
  }

  def fromDate(year: Int, month: Int, day: Int): WeekOfYear = {
    val isLeapYear = year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)
    val daysInMonths = Map(
      1 -> 31,
      2 -> (if (isLeapYear) 29 else 28),
      3 -> 31,
      4 -> 30,
      5 -> 31,
      6 -> 30,
      7 -> 31,
      8 -> 31,
      9 -> 30,
      10 -> 31,
      11 -> 30
    )
    val daysOfPreviousMonths = (1 until month).map(daysInMonths).sum
    val daysFromStartOfYear = day + daysOfPreviousMonths
    WeekOfYear(daysFromStartOfYear / 7 + 1, year)
  }
}

case class WeekOfYearCarrierFromTo(weekOfYear: WeekOfYear,
                                   carrier: String,
                                   from: String,
                                   to: String)
