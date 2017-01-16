package iosr.preprocessing

import iosr.models._
import org.apache.spark.sql._

object AirportsPreprocessing {
  def main(args: Array[String]): Unit = {
    val dataDir = System.getenv("DATA_DIR")
    val spark = SparkSession.builder.appName("AirportsPreprocessing").getOrCreate
    val files = s"$dataDir/*.csv"

    import spark.implicits._

    spark.read.format("csv").load(files)
      .filter((row: Row) => !row.anyNull)
      .flatMap((row: Row) => {
        val year = row.getString(0).toInt
        val month = row.getString(1).toInt
        val day = row.getString(2).toInt
        val from = row.getString(4)
        val to = row.getString(5)
        val weekOfYear = WeekOfYear.fromDate(year, month, day)
        Seq(
          (WeekOfYearAirport(weekOfYear, Airport(from)), 1),
          (WeekOfYearAirport(weekOfYear, Airport(to)), 1)
        )
      })
      .groupByKey(_._1)
      .reduceGroups(sumNums)
      .map({ case (key, (_, num)) =>
        (key.weekOfYear.year, key.weekOfYear.week, key.airport.code, num)
      })
      .write.csv(s"$dataDir/output/airports")
  }

  type ReduceArgument = (WeekOfYearAirport, Int)

  private val sumNums: (ReduceArgument, ReduceArgument) => ReduceArgument = {
    case ((key, num1), (_, num2)) => (key, num1 + num2)
  }
}
