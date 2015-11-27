package time

import com.google.inject.ImplementedBy
import org.joda.time.DateTime

@ImplementedBy(classOf[NowProviderImpl])
trait NowProvider {
  def now: DateTime
}

class NowProviderImpl extends NowProvider {
  override def now: DateTime = DateTime.now
}