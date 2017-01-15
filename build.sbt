import CommonSettings._

name := "iosr-distributed-data-processing"

version := "1.0"

scalaVersion := "2.11.8"

lazy val common = project in file("common")

lazy val preprocessing = (project in file("preprocessing")).dependsOn(common).settings(commonSettings)

lazy val `iosr-distributed-data-processing` = (project in file(".")).aggregate(common, preprocessing)
