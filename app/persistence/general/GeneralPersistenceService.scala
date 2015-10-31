package persistence.general

import model.Video

trait GeneralPersistenceService extends RatingPersistence with VideoPersistence

trait RatingPersistence {
  def ratingFor(userId: Int, titleId: Int, ratingType: RatingType): Option[Double]
  def setRating(userId: Int, titleId: Int, ratingType: RatingType, rating: Double): Unit
  def cancelRating(userId: Int, titleId: Int, ratingType: RatingType): Unit
  def countRatingsBy(userId: Int, ratingType: RatingType): Int
}

trait VideoPersistence {
  def videosFor(titleId: Int): Seq[Video]
  def saveVideo(titleId: Int, userId: Int, video: Video): Unit
}

sealed trait RatingType {
  def discriminator: String
}
case object LikeRating extends RatingType {
  override def discriminator: String = "LR"
}
case object KnowRating extends RatingType {
  override def discriminator: String = "KR"
}
