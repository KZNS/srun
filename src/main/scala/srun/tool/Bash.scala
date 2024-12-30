package srun.tool

object Bash {
  def apply(cmd: String) = {
    os.proc("bash", "-c", cmd)
  }
}
object callBash {
  def apply(cmd: String) = {
    Bash(cmd).call(stdin = os.Inherit, stdout = os.Inherit, stderr = os.Inherit)
  }
}
