package srun.tool

case class Bash(cmd: String) {
  val proc = os.proc("bash", "-c", cmd)

  def run(echo: Boolean = true) = {
    if (echo) println(cmd)
    proc.call(stdin = os.Inherit, stdout = os.Inherit, stderr = os.Inherit)
  }

  export proc.*
}
