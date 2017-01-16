import CommonSettings._

name := "iosr-distributed-data-processing"

version := "1.0"

scalaVersion := "2.11.8"

lazy val common = (project in file("common"))
  .settings(commonSettings)

lazy val preprocessing = (project in file("preprocessing"))
  .dependsOn(common)
  .settings(commonSettings)
  .settings(libraryDependencies ++= sparkDependencies)

lazy val application = (project in file ("application"))
  .dependsOn(common)
  .settings(commonSettings)
  .enablePlugins(PlayScala)

lazy val `iosr-distributed-data-processing` = (project in file("."))
  .aggregate(common, preprocessing, application)
