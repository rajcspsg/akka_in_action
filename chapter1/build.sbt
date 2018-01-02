version := "1.0"

scalaVersion := "2.12.4"

name := "Up and Running"

description := "Up and Running"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.8",
  "com.typesafe.akka" %% "akka-stream" % "2.5.8",
  "com.typesafe.akka" %% "akka-http" % "10.1.0-RC1",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.0-RC1",
  "com.typesafe.akka" %% "akka-slf4j" % "2.5.8",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  //"io.spray" %% "spray-json" % "1.3.3",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.8" % "test",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test"
)
