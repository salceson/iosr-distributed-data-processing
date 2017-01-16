import sbt._
import Keys._

object CommonSettings {
  private val SparkVersion = "2.1.0"

  val sparkDependencies: Seq[ModuleID] = Seq(
    "org.apache.spark" %% "spark-core" % SparkVersion,
    "org.apache.spark" %% "spark-sql" % SparkVersion
  )

  val commonLibDependencies: Seq[ModuleID] = Seq(
    "com.github.nscala-time" %% "nscala-time" % "2.16.0"
  )

  val commonTestScalacSettings: Seq[String] = Seq("-Yrangepos")

  val commonResolvers: Seq[MavenRepository] = Seq(
    "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"
  )

  val commonSettings: Seq[Def.Setting[_]] = Seq(
    resolvers ++= commonResolvers,
    scalacOptions in Test ++= commonTestScalacSettings,
    libraryDependencies ++= commonLibDependencies
  )
}
