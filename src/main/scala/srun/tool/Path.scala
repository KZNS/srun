package srun.tool

object ExpandPath {
  def apply(path: String): os.Path = {
    if path.startsWith("~") then os.Path.expandUser(path)
    else if path.startsWith("/") then os.Path(path)
    else os.pwd / os.RelPath(path)
  }
}

def filesUnder(path: os.Path, exts: Set[String] = Set.empty): Set[os.Path] = {
  os.walk(path)
    .filter(x => os.isFile(x) && (exts.isEmpty || exts.contains(x.ext)))
    .toSet
}
def filesUnder(path: os.Path, ext: String): Set[os.Path] = filesUnder(path, Set(ext))
