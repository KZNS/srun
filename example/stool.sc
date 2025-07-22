#!/usr/bin/env -S scala-cli shebang

// run ```sbt assembly``` to generate the jar file
//> using jar ../target/scala-3.5.0/srun-assembly-0.1.0-SNAPSHOT.jar
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.tool.*

// Bash
Bash("echo 2333").run()

// Charset
val utf8Str = "你好这是一段的文本，但是短了有识别率问题，暂时保留"
println(Charset.detect(utf8Str.getBytes("GB2312")))
println(String(utf8Str.getBytes("GB2312"), "GB18030"))

// CSV

val csvTable = """a,b,c
1,2,3""".toCSVTable
assert(csvTable.head(1) == "b")

// DateTime

val dateTime = "2024-12-23 22:24:50".toLocalDateTime("yyyy-MM-dd HH:mm:ss")
assert(dateTime.getHour() == 22)
assert(dateTime.toString("yyMMdd-HHmmss") == "241223-222450")

// FileString
val bomStr = "\uFEFFHello, World!"
assert(bomStr.hasBom)
assert(!bomStr.removedBom.hasBom)
assert(bomStr.removedBom.addedBom.hasBom)

// GitHubRepo
GitHubRepo("kzns", "srun").downloadFile(".scalafmt.conf", "build/.scalafmt.conf", Some("//"))

// Java
val javaList  = java.util.List.of("2333", "2334", "2335")
val scalaList = javaList.asScalaAll
println(scalaList)

// Parallel
val nums = scala.collection.parallel.immutable.ParSeq((1 to 5).toList*)
nums
  .withLimit(2)
  .map { i =>
    Thread.sleep(1000)
    println(i)
  }

// Path
println(filesUnder(os.pwd / "src", "scala"))
println(!Seq(os.pwd / "build/world.txt").forallNewerThan(Seq(os.pwd / "build/hello.txt")))
println((os.SubPath("a") / "b" / "c.txt").replaceExt("out"))
println(os.pwd.addExt("777"))

// Self
val data = "2333"
data.selfCall(println).selfMap(_.toInt + 1).selfCall(println)

// WebString
val filePath =
  "/Downloads/The.Wolf.of.Wall.Street.chs%26amp%3Beng%5B%E7%89%B9%E6%95%88%5D.ass"
println(filePath.urlDecode.unescapeEntities)
