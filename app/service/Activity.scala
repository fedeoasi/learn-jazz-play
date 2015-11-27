package service

import model.{Video, Title}
import org.joda.time.DateTime

sealed trait Activity {
  def timestamp: DateTime
  def description: String
  def toBaseActivity: SerializedActivity = {
    SerializedActivity(timestamp.toString("YYYY-MM-dd hh:mm"), description)
  }
}

case class SerializedActivity(timestamp: String, description: String)

case class RatedTitle(timestamp: DateTime,
                      title: Title) extends Activity {
  override def description: String = {
    s"Rated title ${title.title}"
  }
}

case class EnteredVideo(timestamp: DateTime,
                        video: Video) extends Activity {
  override def description: String = {
    s"Entered video ${video.videoId}"
  }
}
