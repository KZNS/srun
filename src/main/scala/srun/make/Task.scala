package srun.make

import scala.sys.process._

type TaskName = String
extension (taskName: TaskName) {
  def asTask: Task = Task(name = Some(taskName))
}

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
