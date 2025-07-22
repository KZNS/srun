#!/usr/bin/env -S scala-cli shebang
//> using repository jitpack
//> using dep com.github.kzns:srun:9efde4c

import srun.tool.*

val filePath = args.headOption match
  case Some(fileName) => expandPath(fileName)
  case None => throw new IllegalArgumentException("No file name provided. Please provide a file name as an argument.")

val commitId = Bash(
  """curl -s https://api.github.com/repos/kzns/srun/branches/main | grep -o '"sha": *"[^"]*"' | head -n 1 | sed 's/"sha": "//;s/"$//' | cut -c1-7"""
).proc.call().out.trim()

val head =
  s"""|#!/usr/bin/env -S scala-cli shebang
      |//> using repository jitpack
      |//> using dep com.github.kzns:srun:$commitId
      |
      |import srun.tool.*
      |""".stripMargin

os.write.over(filePath, head)
Bash(s"chmod +x $filePath").call()
