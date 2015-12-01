package service

import model.Rating
import model.RatingTypes._
import org.scalatest.{FunSpec, Matchers}
import persistence.PersistenceSpecHelper._
import persistence.auth.AuthSpecHelper._
import persistence.general.GeneralPersistenceService
import titles.TitleSpecHelper._

class RatingServiceSpec extends FunSpec with Matchers {
  describe("Favorite titles (LikeRatings)") {
    it("returns an empty list of ratings") {
      val service = withRatingService()
      service.favoriteTitles(user) shouldBe Seq.empty
    }

    it("ignores other types of ratings") {
      val service = withRatingService { gps =>
        gps.setRating(user.id, title.id, KnowRating, 3)
      }
      service.favoriteTitles(user) shouldBe Seq.empty
    }

    it("returns a rating") {
      val service = withRatingService { gps =>
        gps.setRating(user.id, title.id, LikeRating, 2)
      }
      val rating = Rating(title.id, 2, LikeRating, now)
      val titleWithRating = TitleWithRating(title, rating)
      service.favoriteTitles(user) shouldBe Seq(titleWithRating)
    }

    it("sorts by rating descending") {
      val service = withRatingService { gps =>
        gps.setRating(user.id, title.id, LikeRating, 2)
        gps.setRating(user.id, title2.id, LikeRating, 3)
      }
      val rating = Rating(title.id, 2, LikeRating, now)
      val rating2 = Rating(title2.id, 3, LikeRating, now)
      val titleWithRating = TitleWithRating(title, rating)
      val titleWithRating2 = TitleWithRating(title2, rating2)
      service.favoriteTitles(user) shouldBe Seq(titleWithRating2, titleWithRating)
    }
  }

  private def withRatingService(f: GeneralPersistenceService => Unit = _ => Unit): RatingService = {
    val generalPersistenceService = buildPersistenceService()
    f(generalPersistenceService)
    new RatingServiceImpl(titleRepository, generalPersistenceService)
  }
}
