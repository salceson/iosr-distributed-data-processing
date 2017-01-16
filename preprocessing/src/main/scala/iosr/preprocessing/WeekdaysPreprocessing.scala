package iosr.preprocessing

import iosr.models._
import org.apache.spark.sql._

object WeekdaysPreprocessing {
  def main(args: Array[String]): Unit = {
    val dataDir = System.getenv("DATA_DIR")
    val spark = SparkSession.builder.appName("WeekdaysPreprocessing").getOrCreate
    val files = s"$dataDir/*.csv"

    import spark.implicits._

    spark.read.format("csv").load(files)
      .filter((row: Row) => !row.anyNull)
      .map((row: Row) => {
        val year = row.getString(0).toInt
        val month = row.getString(1).toInt
        val day = row.getString(2).toInt
        val departureDelay = row.getString(6).toDouble
        val arrivalDelay = row.getString(7).toDouble
        val weekOfYear = WeekOfYear.fromDate(year, month, day)
        val weekday = Weekday.fromDate(year, month, day)
        (WeekOfYearWeekday(weekOfYear, weekday), departureDelay, arrivalDelay, 1)
      })
      .groupByKey(_._1)
      .reduceGroups(sumDelaysAndNums)
      .map({ case (key, (_, departureDelay, arrivalDelay, num)) =>
        (key.weekOfYear.year, key.weekOfYear.week, key.weekday.dayOfWeek, departureDelay, arrivalDelay, num)
      })
      .write.csv(s"$dataDir/output/weekdays")
  }

  type ReduceArgument = (WeekOfYearWeekday, Double, Double, Int)

  private val sumDelaysAndNums: (ReduceArgument, ReduceArgument) => ReduceArgument = {
    case ((key, departureDelay1, arrivalDelay1, num1), (_, departureDelay2, arrivalDelay2, num2)) =>
      (key, departureDelay1 + departureDelay2, arrivalDelay1 + arrivalDelay2, num1 + num2)
  }
}
