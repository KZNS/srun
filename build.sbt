ThisBuild / scalaVersion := "3.5.0"
ThisBuild / version      := "0.1.0-SNAPSHOT"
ThisBuild / organization := "com.kzns"

lazy val srun = (project in file("."))
  .settings(
    name := "srun",
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "requests" % "0.9.0",
      "com.lihaoyi" %% "upickle"  % "4.0.2",
      "com.lihaoyi" %% "os-lib"   % "0.11.3",
      // for WebString
      "org.jsoup" % "jsoup" % "1.18.3",
      // for Charset.detect
      "org.apache.tika" % "tika-parsers-standard-package" % "3.0.0"
    ),
    scalacOptions ++= Seq()
  )

ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", "versions", _ @_*) => MergeStrategy.first
  case PathList("module-info.class")           => MergeStrategy.discard
  case PathList("META-INF", _ @_*)             => MergeStrategy.discard
  case x if x.endsWith(".class")               => MergeStrategy.first
  case _                                       => MergeStrategy.deduplicate
}
