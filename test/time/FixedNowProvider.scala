package time

import org.joda.time.DateTime

case class FixedNowProvider(override val now: DateTime) extends NowProvider
