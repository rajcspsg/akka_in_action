name := "akka in action"

version := "1.0"

scalaVersion := "2.12.4"

fork in run := true


lazy val chapter2 = (project in file("chapter2")).settings(
  name := "chapter2",
  version := "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter4 = (project in file("chapter4")).settings(
  name := "chapter4",
  version := "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter6 = (project in file("chapter6")).settings(
  name := "chapter6",
  version := "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter6_boxofficeRemote = (project in file("chapter6_boxofficeRemote")).settings(
  name := "chapter6_boxofficeRemote",
  version := "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter6_RemoteDeploy = (project in file("chapter6_RemoteDeploy")).settings(
  name := "chapter6_RemoteDeploy",
  version := "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter6_remoteDeployWithForwarder = (project in file("chapter6_remoteDeployWithForwarder")).settings(
  name := "chapter6_remoteDeployWithForwarder",
  version := "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter8_structuralPatterns = (project in file("chapter8_structuralPatterns")).settings(
  name:= "chapter8_structuralPatterns",
  version:= "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter10_messageChannels = (project in file("chapter10_messageChannels")).settings(
  name:= "chapter10_messageChannels",
  version:= "1.0",
  scalaVersion := "2.12.4"
)

lazy val chapter14 = (project in file("chapter14")).settings(
  name := "chapter14",
  version := "1.0",
  scalaVersion := "2.12.4"
)

