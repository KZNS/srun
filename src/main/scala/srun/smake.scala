package srun

import scala.annotation.targetName
import scala.sys.process._

object Smake {
  val toolName = "smake"

  def smakePrintln(msg: String): Unit = println(s"$toolName: $msg")

  case class Target(path: String) {
    lazy val filePath   = java.nio.file.Paths.get(path)
    def exists: Boolean = java.nio.file.Files.exists(filePath)
    def lastModifiedTime: Long =
      if (!exists) 0
      else java.nio.file.Files.getLastModifiedTime(filePath).toMillis
  }

  case class Job(tasks: Seq[Task]) {
    val nameMap: Map[TaskName, Task] =
      tasks.flatMap(t => t.name.map(_ -> t)).toMap
    val targetMap: Map[Target, Task] =
      tasks.flatMap(t => t.targets.map(_ -> t)).toMap
    val taskDepGraph: Map[Task, Set[Task]] = tasks
      .map(t => t -> (t.deps.flatMap(targetMap.get).toSet ++ t.depTasks.map(nameMap)))
      .toMap
    def getRunSeq(task: Task): Seq[Task] = {
      import scala.collection.mutable
      val depCount = mutable.Map(task -> 0)
      def updateDepCount(t: Task): Unit = {
        val needExtend = taskDepGraph(t).filterNot(depCount.contains)
        taskDepGraph(t).foreach(dep => depCount.updateWith(dep)(_.map(_ + 1).orElse(Some(1))))
        needExtend.foreach(updateDepCount)
      }
      updateDepCount(task)
      var runList = List.empty[Task]
      def extend(t: Task): Unit = {
        runList = t :: runList
        taskDepGraph(t).foreach { dep =>
          depCount.updateWith(dep)(_.map(_ - 1).orElse(Some(0)))
          if (depCount(dep) == 0) extend(dep)
        }
      }
      extend(task)
      runList.toSeq
    }
    def runByName(name: TaskName): Unit = {
      println(s"$toolName: runByName $name")
      nameMap.get(name) match
        case Some(task) => runTaskWithDepTasks(task)
        case None       => throw IllegalArgumentException(s"$toolName: task `$name` not found")
    }
    def runByTarget(target: Target): Unit = {
      println(s"$toolName: runByTarget $target")
      targetMap.get(target) match
        case Some(task) => runTaskWithDepTasks(task)
        case None       => throw IllegalArgumentException(s"$toolName: target `${target.path}` not found")
    }
    def runTask(task: Task): Unit = {
      require(tasks.contains(task), s"$toolName: task $task not in this jobs")
      println(s"$toolName: runTask $task")
      runTaskWithDepTasks(task)
    }
    private def runTaskWithDepTasks(task: Task): Unit = {
      getRunSeq(task).foreach(_.run(using this))
    }

    def asMain(args: Array[String]): Unit = {
      args.headOption match
        case None =>
          tasks.headOption match
            case Some(task) => runTask(task)
            case None       => println(s"$toolName: no task to run")
        case Some(name) => runByName(name)
    }
  }
  object Job {
    @targetName("applyExpandedTasks")
    def apply(tasks: Task*): Job = Job(tasks)
  }

  type TaskName = String
  case class Task(
      name: Option[TaskName] = None,
      targets: Set[Target] = Set.empty,
      deps: Set[Target] = Set.empty,
      depTasks: Set[TaskName] = Set.empty,
      runs: Seq[Run] = Seq.empty
  ) {
    lazy val targetsFirstModifiedTime: Long = targets.map(_.lastModifiedTime).minOption.getOrElse(0)
    lazy val depsLastModifiedTime: Long     = deps.map(_.lastModifiedTime).maxOption.getOrElse(0)

    def run(using Job): Unit =
      if (depsLastModifiedTime >= targetsFirstModifiedTime) {
        smakePrintln(s"task running $this")
        runs.foreach(_.run)
      } else {
        smakePrintln(s"task $this is up-to-date, skipped")
      }

    def setName(name: TaskName): Task       = copy(name = Some(name))
    def setTargets(paths: String*): Task    = copy(targets = paths.map(Target(_)).toSet)
    def setDeps(paths: String*): Task       = copy(deps = paths.map(Target(_)).toSet)
    def setDepTasks(names: TaskName*): Task = copy(depTasks = names.toSet)
    def setRuns(runs: Run*): Task           = copy(runs = runs.toSeq)

    override def toString(): String = name match
      case Some(name) => s"Task($name)"
      case None =>
        if targets.size == 1
        then s"Task(${targets.head})"
        else s"Task(${targets.head}s)"
  }
  object Task {
    def apply(name: TaskName): Task = Task(name = Some(name))
  }

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

  class RunTaskByName(name: TaskName) extends Run {
    def run(using job: Job): Unit = {
      job.runByName(name)
    }
  }
}
