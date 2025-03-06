#!/usr/bin/env -S scala-cli shebang

// run ```sbt assembly``` to generate the jar file
//> using jar "../target/scala-3.5.0/srun-assembly-0.1.0-SNAPSHOT.jar"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

// Concurrent and Parallel

val nums = (1 to 5).toList

import srun.shortcut.Concurrent.*
val futures = nums.map: i =>
  Future {
    Thread.sleep(1000);
    println("concurrent " + i)
  }

import srun.shortcut.Parallel.*
nums.par.map("parallel " + _).foreach(println)

Await.result(Future.sequence(futures), Duration.Inf)
