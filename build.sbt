import com.typesafe.sbt.less.Import.LessKeys
import play.PlayScala

name := """learn-jazz-play"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += Resolver.sonatypeRepo("snapshots")

lazy val securesocial = "ws.securesocial" %% "securesocial" % "3.0-M3"
lazy val webJarsPlay = "org.webjars" %% "webjars-play" % "2.3.0-2"
lazy val webJarsBootstrap = "org.webjars" % "bootstrap" % "3.1.1-2"
lazy val scalatestPlus = "org.scalatestplus" %% "play" % "1.2.0" % "test"
lazy val webJarsHandlebars = "org.webjars.bower" % "handlebars" % "2.0.0"
lazy val webJarsJQuery = "org.webjars" % "jquery" % "2.1.4"
lazy val webJarsDatatables = "org.webjars" % "datatables" % "1.10.9"
lazy val webJarsDatatablesTools = "org.webjars" % "datatables-tools" % "2.2.4-1"

lazy val slick = "com.typesafe.slick" %% "slick" % "2.1.0"
lazy val sqlite = "org.xerial" % "sqlite-jdbc" % "3.8.7"
lazy val jodaMapper = "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0"
lazy val h2 = "com.h2database" % "h2" % "1.4.185"
lazy val flyway = "org.flywaydb" % "flyway-core" % "3.2.1"
lazy val dbDependencies = Seq(slick, sqlite, jodaMapper, flyway)

lazy val guice = "com.google.inject" % "guice" % "3.0"
lazy val javaInject = "javax.inject" % "javax.inject" % "1"
lazy val diDependencies = Seq(guice, javaInject)

lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.2.11"
lazy val csv = "com.github.tototoshi" %% "scala-csv" % "1.1.2"

lazy val jsoup = "org.jsoup" % "jsoup" % "1.8.3"

libraryDependencies ++= Seq(
  securesocial,
  webJarsPlay,
  webJarsBootstrap,
  webJarsHandlebars,
  webJarsJQuery,
  webJarsDatatables,
  webJarsDatatablesTools,
  scalatestPlus,
  json4s,
  csv,
  jdbc,
  anorm,
  cache,
  ws,
  jsoup
) ++ dbDependencies ++ diDependencies

includeFilter in (Assets, LessKeys.less) := "*.less"

excludeFilter in (Assets, LessKeys.less) := "_*.less"

LessKeys.compress := true

LessKeys.verbose := true
