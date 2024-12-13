package srun.make

import scala.sys.process._

abstract class Run() {
  def run(using Job): Unit
}

class RunFunc(func: () => Unit) extends Run {
  def run(using Job): Unit = func()
}

class RunCmd(cmd: String) extends Run {
  def run(using Job): Unit = {
    smakePrintln(s"run cmd `$cmd`")
    val ret = Seq("bash", "-c", cmd).!
    assert(ret == 0, s"command `$cmd` failed with exit code $ret")
  }
}

class WriteFile(str: String, filePath: String, option: String = "w") extends Run {
  def run(using Job): Unit = {
    smakePrintln(s"write \"$str\" to file `$filePath`")
    import java.nio.file._
    val standardOpenOptions = {
      import StandardOpenOption._
      option match {
        case "w" => Seq(WRITE, CREATE, TRUNCATE_EXISTING)
        case "a" => Seq(APPEND, CREATE)
      }
    }
    val path = Paths.get(
      if filePath.startsWith("~")
      then filePath.replaceFirst("~", System.getProperty("user.home"))
      else filePath
    )

    path.getParent.toFile.mkdirs()
    Files.write(path, str.getBytes(), standardOpenOptions*)
  }
}
class RunTaskByName(name: TaskName) extends Run {
  def run(using job: Job): Unit = {
    job.runByName(name)
  }
}
