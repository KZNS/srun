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
    os.proc("bash", "-c", cmd)
      .call(stdin = os.Inherit, stdout = os.Inherit, stderr = os.Inherit)
  }
}

class WriteFile(path: String, str: String, option: String = "w") extends Run {
  def run(using Job): Unit = {
    smakePrintln(s"write to file `$path`: \"$str\"")
    val absPath = srun.tool.ExpandPath(path)
    option match {
      case "w" =>
        os.write.over(absPath, str, createFolders = true)
      case "a" =>
        os.write.append(absPath, str, createFolders = true)
    }
  }
}
class RunTaskByName(name: TaskName) extends Run {
  def run(using job: Job): Unit = {
    job.runByName(name)
  }
}
