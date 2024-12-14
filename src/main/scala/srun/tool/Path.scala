package srun.tool

object ExpandPath {
  def apply(path: String): os.Path = {
    if path.startsWith("~") then os.Path.expandUser(path)
    else if path.startsWith("/") then os.Path(path)
    else os.pwd / os.RelPath(path)
  }
}
