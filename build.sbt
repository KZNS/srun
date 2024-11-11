ThisBuild / scalaVersion := "3.5.0"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.kzns"

lazy val srun = (project in file("."))
  .settings(
    name := "srun",
    libraryDependencies ++= Seq(),
    scalacOptions ++= Seq()
  )
