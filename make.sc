#!/usr/bin/env -S scala-cli shebang

import scala.sys.process._

def smakePrintln(str: String) = println("smake: " + str)

def runCmd(cmd: String): Int = {
  smakePrintln(s"run `$cmd`")
  Seq("bash", "-c", cmd).!
}
case class Run(func: () => Int) {
  def apply(): Int = func()
}
class Task(val name: String, val runs: Seq[Run]) {
  def apply(): Unit = {
    smakePrintln(s"running task `$name`")
    runs.foreach(_.apply())
  }
}
object Task {
  def apply(name: String)(runs: Run*): Task = new Task(name, runs)
}
implicit def stringToRun(cmd: String): Run = Run(() => runCmd(cmd))
implicit def funcToRun(func: => Int): Run = Run(() => func)
implicit class StringTaskName(name: String) {
  def dose(runs: Run*): Task = new Task(name, runs)
}

class Jobs(val tasks: Seq[Task]) {
  def toSeq: Seq[Task] = tasks
  def run(name: String): Unit = tasks.find(_.name == name) match {
    case None       => println(s"Task \"$name\" not found")
    case Some(task) => task.apply()
  }
}
object Jobs {
  def apply(tasks: Task*): Jobs = new Jobs(tasks)
}
implicit def JobsToSeq(jobs: Jobs): Seq[Task] = jobs.toSeq
implicit def SeqToJobs(tasks: Seq[Task]): Jobs = new Jobs(tasks)

// my config

val jobs = Jobs(
  "hello".dose("echo hello")
)

jobs.run(args(0))
