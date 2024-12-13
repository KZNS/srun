#!/usr/bin/env -S scala-cli shebang

// run ```sbt assembly``` to generate the jar file
//> using jar "../target/scala-3.5.0/srun-assembly-0.1.0-SNAPSHOT.jar"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.tool._

GitHubRepo("kzns", "srun").downloadFile(".scalafmt.conf", "build/.scalafmt.conf", Some("//"))
