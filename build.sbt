ThisBuild / scalaVersion := "3.5.0"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.kzns"

lazy val srun = (project in file("."))
  .settings(
    name := "srun",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.9.0",
      "com.lihaoyi" %% "upickle"  % "4.0.2",
      "com.lihaoyi" %% "os-lib"   % "0.11.3"
    ),
    scalacOptions ++= Seq()
  )
