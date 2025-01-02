package srun.tool

import com.github.tototoshi.csv._
import scala.util._

extension [T](data: List[List[T]]) {
  def toCSVString: String = {
    val w = new java.io.StringWriter()
    Using(CSVWriter.open(w))(_.writeAll(data))
    w.toString()
  }
}
extension (csv: String) {
  def toCSVTable: List[List[String]] = {
    Using(CSVReader.open(new java.io.StringReader(csv)))(_.all()).get
  }
}
