package srun.tool

import os._
import requests._

case class GitHubRepo(owner: String, repo: String, version: Git.CodeVersion = Git.Branch("main")) {
  def downloadFile(pathInRepo: String, dest: String, addSourceInFile: Option[String] = None): Unit = {
    val url     = fileUrl(pathInRepo)
    val r       = requests.get(url)
    val absPath = os.pwd / os.RelPath(dest)
    addSourceInFile match {
      case Some(commentHead) =>
        os.write.over(
          absPath,
          s"""|${r.text()}
              |$commentHead from https://github.com/$owner/$repo
              |$commentHead $version $commitId
              |$commentHead $pathInRepo
              |""".stripMargin
        )
      case None =>
        os.write.over(absPath, r.bytes)
    }
  }
  def fileUrl(path: String): String = {
    val versionStr = version match
      case Git.Branch(branch) => s"refs/heads/$branch"
      case Git.Commit(sha)    => sha
      case Git.Tag(tag)       => s"refs/tags/$tag"
    s"https://raw.githubusercontent.com/$owner/$repo/$versionStr/$path"
  }
  def commitId: String = {
    import requests._
    import upickle._
    version match
      case Git.Commit(sha) => {
        val r    = requests.get(s"https://api.github.com/repos/$owner/$repo/commits/$sha")
        val json = ujson.read(r.text())
        json.obj("sha").str
      }
      case Git.Branch(branch) => {
        val r    = requests.get(s"https://api.github.com/repos/$owner/$repo/branches/$branch")
        val json = ujson.read(r.text())
        json.obj("commit").obj("sha").str
      }
      case Git.Tag(tag) => {
        val r    = requests.get(s"https://api.github.com/repos/$owner/$repo/tags")
        val json = ujson.read(r.text())
        json.arr.find(_("name").str == tag).get.obj("commit").obj("sha").str
      }
  }
}

object Git {
  sealed abstract class CodeVersion
  case class Branch(branch: String) extends CodeVersion
  case class Commit(sha: String)    extends CodeVersion
  case class Tag(tag: String)       extends CodeVersion
}
