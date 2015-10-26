import play.PlayScala

name := """courses-app"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val securesocial = "ws.securesocial" %% "securesocial" % "3.0-M3"
lazy val webJarsPlay = "org.webjars" %% "webjars-play" % "2.3.0-2"
lazy val webJarsBootstrap = "org.webjars" % "bootstrap" % "3.1.1-2"
lazy val scalatestPlus = "org.scalatestplus" %% "play" % "1.2.0" % "test"

lazy val slick = "com.typesafe.slick" %% "slick" % "2.1.0"
lazy val sqlite = "org.xerial" % "sqlite-jdbc" % "3.8.7"
lazy val jodaMapper = "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
lazy val h2 = "com.h2database" % "h2" % "1.4.185"
lazy val dbDependencies = Seq(slick, sqlite, jodaMapper, h2)

lazy val guice = "com.google.inject" % "guice" % "3.0"
lazy val javaInject = "javax.inject" % "javax.inject" % "1"
lazy val diDependencies = Seq(guice, javaInject)

lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.2.11"
lazy val csv = "com.github.tototoshi" %% "scala-csv" % "1.1.2"

libraryDependencies ++= Seq(
  securesocial,
  webJarsPlay,
  webJarsBootstrap,
  scalatestPlus,
  json4s,
  csv,
  jdbc,
  anorm,
  cache,
  ws
) ++ dbDependencies ++ diDependencies
