package srun.tool

extension [T <: Any](data: T) {
  def selfCall(f: T => Any): T = {
    f(data)
    data
  }
  def selfMap[S <: Any](f: T => S) = {
    f(data)
  }
}
