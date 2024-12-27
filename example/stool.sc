#!/usr/bin/env -S scala-cli shebang

// run ```sbt assembly``` to generate the jar file
//> using jar "../target/scala-3.5.0/srun-assembly-0.1.0-SNAPSHOT.jar"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.tool._

// GitHubRepo
GitHubRepo("kzns", "srun").downloadFile(".scalafmt.conf", "build/.scalafmt.conf", Some("//"))

// WebString
val filePath =
  "/Downloads/The.Wolf.of.Wall.Street.chs%26amp%3Beng%5B%E7%89%B9%E6%95%88%5D.ass"
println(filePath.urlDecode.unescapeEntities)
