package service

import com.google.inject.{ImplementedBy, Inject}
import model.RatingTypes._
import model.{Rating, Title}
import persistence.general.GeneralPersistenceService
import titles.TitleRepository

case class TitleWithRating(title: Title, rating: Rating)

@ImplementedBy(classOf[RatingServiceImpl])
trait RatingService {
  def favoriteTitles(user: User): Seq[TitleWithRating]
}

class RatingServiceImpl @Inject() (titleRepository: TitleRepository,
                                   generalPersistenceService: GeneralPersistenceService) extends RatingService {
  override def favoriteTitles(user: User): Seq[TitleWithRating] = {
    val ratings = generalPersistenceService.ratingsFor(user.id)
      .filter(_.ratingType == LikeRating)
      .sortBy(_.rating)
      .reverse
    ratings.map { r =>
      TitleWithRating(titleRepository(r.titleId), r)
    }
  }
}
