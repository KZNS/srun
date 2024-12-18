package srun.make

case class Job(tasks: Seq[Task]) {
  val nameMap: Map[TaskName, Task] =
    tasks.flatMap(t => t.name.map(_ -> t)).toMap
  val absPathMap: Map[os.Path, Task] =
    tasks.flatMap(t => t.targets.map(_.absPath -> t)).toMap
  val taskDepGraph: Map[Task, Set[Task]] = tasks
    .map(t => t -> (t.deps.flatMap(t => absPathMap.get(t.absPath)).toSet ++ t.depTasks.map(nameMap)))
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

  def asMain(args: Array[String]): Unit = {
    args.headOption match
      case None =>
        tasks.headOption match
          case Some(task) => runTask(task)
          case None       => smakePrintln("no task to run")
      case Some(name) => runByName(name)
  }
}
object Job {
  import scala.annotation.targetName
  @targetName("applyExpandedTasks")
  def apply(tasks: Task*): Job = Job(tasks)
}
