package srun.tool

import scala.jdk.CollectionConverters.*

extension (x: Any) {
  def asScalaAll: Any = x match
    case x: java.util.List[_]   => x.asScala.map(_.asScalaAll).toList
    case x: java.util.Map[_, _] => x.asScala.view.mapValues(_.asScalaAll).toMap
    case x                      => x
}
