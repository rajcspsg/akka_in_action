name := "chapter6"
version := "1.0"
scalaVersion := "2.12.4"

description := "Scaling out with Remoting"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-remote" % "2.5.8",
  "com.typesafe.akka" %% "akka-multi-node-testkit" % "2.5.8" % "test"
)
fork in run := true