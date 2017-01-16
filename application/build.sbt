name := "iosr-distributed-data-processing-application"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  json,
  "org.postgresql" % "postgresql" % "9.4.1212"
)
