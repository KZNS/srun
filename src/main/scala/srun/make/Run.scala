package srun.make

import scala.sys.process._

abstract class Run() {
  def runName: String
  def runPrintln(str: String): Unit = {
    smakePrintln(s"- $runName: $str")
  }
  def run(using Job): Unit
}

class RunFunc(func: () => Unit) extends Run {
  def runName              = "RunFunc"
  def run(using Job): Unit = func()
}

class RunCmd(cmd: String) extends Run {
  def runName = "RunCmd"
  def run(using Job): Unit = {
    smakePrintln(s"run cmd `$cmd`")
    os.proc("bash", "-c", cmd)
      .call(stdin = os.Inherit, stdout = os.Inherit, stderr = os.Inherit)
  }
}

class WriteFile(path: String, str: String, option: String = "w", onNeed: Boolean = false) extends Run {
  def runName = "WriteFile"
  def run(using Job): Unit = {
    runPrintln(s"write to file `$path`: \"$str\"")
    val absPath = srun.tool.ExpandPath(path)
    option match {
      case "w" =>
        if onNeed && (os.exists(absPath) && os.read(absPath) == str)
        then runPrintln("no need to write")
        else os.write.over(absPath, str, createFolders = true)
      case "a" =>
        if onNeed && (os.exists(absPath) && str == "")
        then runPrintln("no need to write")
        else os.write.append(absPath, str, createFolders = true)
    }
  }
}
class RunTaskByName(name: TaskName) extends Run {
  def runName = "RunTaskByName"
  def run(using job: Job): Unit = {
    job.runByName(name)
  }
}
