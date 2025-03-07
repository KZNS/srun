package srun.make

case class Target(path: String) {
  val absPath         = os.Path(path, os.pwd)
  def exists: Boolean = os.exists(absPath)
  def lastModifiedTime: Long =
    if (!exists) 0
    else os.mtime(absPath)
}
