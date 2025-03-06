package srun.shortcut

/** Concurrent programming
  *
  * Replace by following imports:
  * {{{
  * import scala.concurrent.*
  * import scala.concurrent.duration.*
  * import scala.concurrent.ExecutionContext.Implicits.global
  * }}}
  */
object Concurrent:
  export scala.concurrent.{Future, Await}
  export scala.concurrent.duration.{
    // in package object
    span,
    fromNow,
    TimeUnit,
    DAYS,
    HOURS,
    MICROSECONDS,
    MILLISECONDS,
    MINUTES,
    NANOSECONDS,
    SECONDS,
    pairIntToDuration,
    pairLongToDuration,
    durationToPair,
    DurationInt,
    DurationLong,
    DurationDouble,
    IntMult,
    LongMult,
    DoubleMult,
    // in package
    Duration
  }
  export scala.concurrent.ExecutionContext.Implicits.global
