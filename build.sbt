lazy val specs2Version = "3.7"

lazy val dependencies = Seq(
  "org.slf4j" % "slf4j-api" % "1.7.18",
  "net.ceedubs" %% "ficus" % "1.1.2",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "org.scalaz" %% "scalaz-core" % "7.2.0",

  //Unit and/or Integration Test Dependencies
  "ch.qos.logback" % "logback-classic" % "1.1.5" % "test",
  "org.specs2" %% "specs2-core" % specs2Version % "test",
  "org.specs2" %% "specs2-mock" % specs2Version % "test",
  "org.specs2" %% "specs2-scalacheck" % specs2Version % "test"
)

lazy val root = (project in file(".")).
  settings(
    organization := "com.garymalouf",
    scalaVersion := "2.11.7",
    name := "averruncus",

    // TODO: Set actual publish settings at some point...
    publish := { },
    publishLocal := {},

    scalacOptions ++= Seq(
      "-unchecked",
      "-deprecation",
      "-feature",
      "-language:postfixOps",
      "-language:implicitConversions",
      "-encoding", "UTF-8",
      "-target:jvm-1.8"),

    libraryDependencies ++= dependencies
  )

coverageExcludedPackages := "com.garymalouf.averruncus.password.example.*"
