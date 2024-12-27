package srun.tool

extension (s: String) {
  def urlDecode: String        = java.net.URLDecoder.decode(s)
  def unescapeEntities: String = org.jsoup.parser.Parser.unescapeEntities(s, false)
}
