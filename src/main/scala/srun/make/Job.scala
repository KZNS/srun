package srun.make

import scalax.collection.edges._
import scalax.collection.immutable.Graph

case class Job(tasks: Seq[Task]) {
  val nameMap: Map[TaskName, Task] =
    tasks.flatMap(t => t.name.map(_ -> t)).toMap
  val absPathMap: Map[os.Path, Task] =
    tasks.flatMap(t => t.targets.map(_.absPath -> t)).toMap
  val taskDepGraph = Graph.from(
    tasks.flatMap: t =>
      (
        t.deps.map(_.absPath).map(absPathMap) ++
          t.depTasks.map(nameMap)
      ).map(t ~> _)
  )
  def getRunSeq(task: Task): Seq[Task] = {
    val depends  = taskDepGraph.get(task).withSubgraph().toSet
    val subgraph = taskDepGraph.filter(depends.contains)

    subgraph.topologicalSort() match {
      case Left(failure) =>
        throw IllegalArgumentException(
          s"$toolName: cycle detected: ${failure.cycle.get.mkString(" -> ")}"
        )
      case Right(sorted) => sorted.toOuter.toSeq.reverse
    }
  }
  def runByName(name: TaskName): Unit = {
    smakePrintln(s"runByName $name")
    nameMap.get(name) match
      case Some(task) => runTaskWithDepTasks(task)
      case None       => throw IllegalArgumentException(s"$toolName: task `$name` not found")
  }
  def runByTarget(target: Target): Unit = {
    smakePrintln(s"runByTarget $target")
    absPathMap.get(target.absPath) match
      case Some(task) => runTaskWithDepTasks(task)
      case None       => throw IllegalArgumentException(s"$toolName: target `${target.path}` not found")
  }
  def runTask(task: Task): Unit = {
    require(tasks.contains(task), s"$toolName: task $task not in this jobs")
    smakePrintln(s"runTask $task")
    runTaskWithDepTasks(task)
  }
  private def runTaskWithDepTasks(task: Task): Unit = {
    getRunSeq(task).foreach(_.run(using this))
  }

  def getTaskNames: Seq[TaskName] = nameMap.keys.toSeq

  def asMain(args: Array[String]): Unit = {
    args.headOption match
      case None =>
        tasks.headOption match
          case Some(task) => runTask(task)
          case None       => smakePrintln("no task to run")
      case Some(name) =>
        name match
          case "-l" => println(getTaskNames.mkString("\n"))
          case _    => runByName(name)
  }
}
object Job {
  import scala.annotation.targetName
  @targetName("applyExpandedTasks")
  def apply(tasks: Task*): Job = Job(tasks)
}
