package model

import model.RatingTypes._
import org.joda.time.DateTime

case class Rating(titleId: Int,
                  rating: Double,
                  ratingType: RatingType,
                  modifiedDate: DateTime)

sealed trait RatingType {
  def discriminator: String
}

object RatingTypes {
  case object LikeRating extends RatingType {
    override def discriminator: String = "LR"
  }
  case object KnowRating extends RatingType {
    override def discriminator: String = "KR"
  }
}

object RatingType {
  val typesByKey = Seq(LikeRating, KnowRating).map { t =>
    (t.discriminator, t)
  }.toMap

  def apply(key: String): RatingType = typesByKey(key)
}
