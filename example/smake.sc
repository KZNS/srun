#!/usr/bin/env -S scala-cli shebang

//> using files "../src/main/scala/"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.Smake.*

Job(
  Task()
    .setTargets("build/hello.txt")
    .setRuns(
      RunCmd("mkdir -p build"),
      RunCmd("echo hello > build/hello.txt")
    ),
  Task("world")
    .setTargets("build/world.txt")
    .setDeps("build/hello.txt")
    .setRuns(
      WriteFile("world\n", "build/world.txt")
    )
).asMain(args)
