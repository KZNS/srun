package srun.tool

extension (s: String) {
  def hasBom: Boolean    = s.startsWith("\uFEFF")
  def removedBom: String = if s.hasBom then s.substring(1) else s
  def addedBom: String   = if s.hasBom then s else "\uFEFF" + s
}
