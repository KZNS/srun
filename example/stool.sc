#!/usr/bin/env -S scala-cli shebang

// run ```sbt assembly``` to generate the jar file
//> using jar "../target/scala-3.5.0/srun-assembly-0.1.0-SNAPSHOT.jar"
// switch to the jitpack and github repo to use outside of the project:
// //> using repository jitpack
// //> using dep "com.github.kzns:srun:f62d4ea"

import srun.tool._

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

// GitHubRepo
GitHubRepo("kzns", "srun").downloadFile(".scalafmt.conf", "build/.scalafmt.conf", Some("//"))

// Path
println(filesUnder(os.pwd / "src", "scala"))
println(!Seq(os.pwd / "build/world.txt").forallNewerThan(Seq(os.pwd / "build/hello.txt")))

// WebString
val filePath =
  "/Downloads/The.Wolf.of.Wall.Street.chs%26amp%3Beng%5B%E7%89%B9%E6%95%88%5D.ass"
println(filePath.urlDecode.unescapeEntities)
