package app

import scala.concurrent.duration.{Duration, FiniteDuration}

trait RequestTimeout {
    lazy val requestTimeout = FiniteDuration(5, scala.concurrent.duration.SECONDS)
}
