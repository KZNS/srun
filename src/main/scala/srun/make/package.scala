package srun.make

private val toolName: String = "smake"
private def smakePrintln(msg: String): Unit = {
  import io.AnsiColor._
  println(s"${CYAN}${toolName}:${RESET} $msg")
}
