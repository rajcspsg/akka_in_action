version := "1.0"

scalaVersion := "2.12.4"

name := "chapter4"

description := "Supervision and Monitoring"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.8",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % "test",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)
