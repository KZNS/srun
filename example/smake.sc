#!/usr/bin/env -S scala-cli shebang

// run ```sbt assembly``` to generate the jar file
//> using jar "../target/scala-3.5.0/srun-assembly-0.1.0-SNAPSHOT.jar"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.make._

Job(
  Task()
    .setTargets("build/hello.txt")
    .setRuns(
      RunCmd("mkdir -p build"),
      RunCmd("echo hello > build/hello.txt")
    ),
  "world".asTask
    .setTargets("build/world.txt")
    .setDeps("build/hello.txt")
    .setRuns(
      WriteFile("build/world.txt", "world\n")
    )
).asMain(args)
