package srun.tool

extension [T, B <: scala.collection.parallel.ParIterable[T]](par: B) {
  def withLimit(limit: Int) = {
    par.tasksupport = new scala.collection.parallel.ForkJoinTaskSupport(
      new java.util.concurrent.ForkJoinPool(limit)
    )
    par
  }
}
