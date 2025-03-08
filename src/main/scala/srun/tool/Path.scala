package srun.tool

def expandPath(path: String, base: os.Path = os.pwd): os.Path = {
  if path.startsWith("~") then os.Path.expandUser(path)
  else if path.startsWith("/") then os.Path(path)
  else base / os.RelPath(path)
}

def filesUnder(path: os.Path, exts: Set[String] = Set.empty): Set[os.Path] = {
  os.walk(path)
    .filter(x => os.isFile(x) && (exts.isEmpty || exts.contains(x.ext)))
    .toSet
}
def filesUnder(path: os.Path, ext: String): Set[os.Path] = filesUnder(path, Set(ext))

extension (files: Iterable[os.Path]) {

  /** Return true if all the `files` are newer than all of the `others`.
    *
    * Newer means `lastModifyTime` is strictly greater.
    */
  def forallNewerThan(others: Iterable[os.Path]): Boolean = {
    files.map(os.mtime).min > others.map(os.mtime).max
  }

  /** Return true if there exists one of the `files` that is newer than one of
    * the `others`.
    *
    * Newer means `lastModifyTime` is strictly greater.
    */
  def existsNewerThan(others: Iterable[os.Path]): Boolean = {
    files.map(os.mtime).max > others.map(os.mtime).min
  }
}

extension [T <: os.FilePath](p: T) {
  def replaceExt(ext: String): T = {
    require(p.ext.nonEmpty, s"`replaceExt` need a file path with extension: p=$p, maybe you want to use `addExt`")
    (p / os.up / s"${p.baseName}.$ext").asInstanceOf[T]
  }
  def addExt(ext: String): T = {
    (p / os.up / s"${p.last}.$ext").asInstanceOf[T]
  }
}
