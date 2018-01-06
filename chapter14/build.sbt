name := "chapter14"
version := "1.0"
scalaVersion := "2.12.4"

description := "Clustering"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-cluster" % "2.5.8"
)

fork in run := true