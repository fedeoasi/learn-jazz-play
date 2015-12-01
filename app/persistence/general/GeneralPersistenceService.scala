package persistence.general

import com.google.inject.ImplementedBy
import model.{RatingType, Rating, VideoInput, Video}
import org.joda.time.DateTime

@ImplementedBy(classOf[GeneralPersistenceServiceImpl])
trait GeneralPersistenceService extends RatingPersistence with VideoPersistence

trait RatingPersistence {
  def ratingFor(userId: Int, titleId: Int, ratingType: RatingType): Option[Rating]
  def ratingsFor(userId: Int): Seq[Rating]
  def setRating(userId: Int, titleId: Int, ratingType: RatingType, rating: Double): Unit
  def cancelRating(userId: Int, titleId: Int, ratingType: RatingType): Unit
  def countRatingsBy(userId: Int, ratingType: RatingType): Int
  def knownTitlesFor(userId: Int): Set[Int]
}

trait VideoPersistence {
  def videosForTitle(titleId: Int): Seq[Video]
  def videosForUser(userId: Int): Seq[Video]
  def saveVideo(titleId: Int, userId: Int, video: VideoInput): Unit
}