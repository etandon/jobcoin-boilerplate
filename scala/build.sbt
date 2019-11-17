import Versions._
name := "gemini-jobcoin-scala"

version := "0.1"

scalaVersion := "2.11.12"


trapExit := false

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.2"
libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.0.0-M1"
libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-json" % "2.0.0-M1"
libraryDependencies += "org.scala-lang.modules" %% "scala-async" % "0.9.7"
libraryDependencies += "de.heikoseeberger" %% "akka-http-circe" % akkaHttpCirceVersion
libraryDependencies += "io.circe" %% "circe-core" % circeVersion
libraryDependencies += "io.circe" %% "circe-generic" % circeVersion
libraryDependencies += "io.circe" %% "circe-parser" % circeVersion
libraryDependencies += "io.circe" %% "circe-generic-extras" % circeVersion
libraryDependencies += "org.julienrf" %% "endpoints-algebra" % endpointsVersion
libraryDependencies += "org.julienrf" %% "endpoints-openapi" % endpointsVersion
libraryDependencies += "org.julienrf" %% "endpoints-akka-http-server" % endpointsVersion
libraryDependencies += "org.julienrf" %% "endpoints-akka-http-server-circe" % endpointsVersion
libraryDependencies += "org.julienrf" %% "endpoints-json-schema-generic" % endpointsVersion
libraryDependencies += "org.julienrf" %% "endpoints-json-schema-circe" % endpointsVersion
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % scalaLoggingVersion
libraryDependencies += "com.github.pureconfig" %% "pureconfig" % pureConfigVersion
libraryDependencies += "ch.qos.logback" % "logback-classic" % logbackClassicVersion

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % "2.5.12" % Test
)
libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.akka" %% "akka-stream-testkit" % "2.5.12" % Test
)
