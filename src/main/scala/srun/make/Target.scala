package srun.make

case class Target(path: String) {
  lazy val filePath   = java.nio.file.Paths.get(path)
  def exists: Boolean = java.nio.file.Files.exists(filePath)
  def lastModifiedTime: Long =
    if (!exists) 0
    else java.nio.file.Files.getLastModifiedTime(filePath).toMillis
}
