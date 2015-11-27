package service

import model.{Video, Title}
import org.joda.time.DateTime

sealed trait Activity {
  def user: User
  def timestamp: DateTime
  def description: String
}

case class RatedTitle(user: User,
                      timestamp: DateTime,
                      title: Title) extends Activity {
  override def description: String = {
    s"Rated title ${title.title}"
  }
}

case class EnteredVideo(user: User,
                        timestamp: DateTime,
                        title: Title,
                        video: Video) extends Activity {
  override def description: String = {
    s"Rated title ${title.title} $video"
  }
}

