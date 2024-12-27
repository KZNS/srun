package srun.tool

object Charset {
  def detect(bytes: Array[Byte]): String = {
    val detector = new org.apache.tika.parser.txt.CharsetDetector()
    detector.setText(bytes)
    val result = detector.detect()
    result.getName()
  }
}
