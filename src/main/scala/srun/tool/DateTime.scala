package srun.tool

export java.time.LocalDateTime

extension (dateTime: java.time.LocalDateTime) {
  def toString(pattern: String): String =
    dateTime.format(java.time.format.DateTimeFormatter.ofPattern(pattern))
}
extension (dateTimeString: String) {
  def toLocalDateTime(pattern: String): java.time.LocalDateTime =
    java.time.LocalDateTime.parse(
      dateTimeString,
      java.time.format.DateTimeFormatter.ofPattern(pattern)
    )
  def toLocalDateTime: java.time.LocalDateTime =
    java.time.LocalDateTime.parse(dateTimeString)
}
