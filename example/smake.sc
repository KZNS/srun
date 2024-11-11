#!/usr/bin/env -S scala-cli shebang

//> using files "../src/main/scala/"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.Smake.*

Job(
  Task(
    None,
    Set(Target("build/hello.txt")),
    Set.empty,
    Set.empty,
    Seq(
      RunCmd("mkdir -p build"),
      RunCmd("echo hello > build/hello.txt")
    )
  ),
  Task(
    Some("world"),
    Set(Target("build/world.txt")),
    Set(Target("build/hello.txt")),
    Set(),
    Seq(RunCmd("echo world > build/world.txt"))
  )
).asMain(args)
