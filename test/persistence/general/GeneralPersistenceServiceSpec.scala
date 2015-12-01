package persistence.general

import model.RatingTypes._
import model.{Rating, VideoInput}
import org.joda.time.DateTime
import org.scalatest.{FunSpec, Matchers}
import time.FixedNowProvider

class GeneralPersistenceServiceSpec extends FunSpec with Matchers {
  val now = DateTime.now

  describe("General Persistence Service") {
    describe("Ratings") {
      it("should not find a non existing rating") {
        val gps = buildPersistenceService()
        gps.ratingFor(1, 1, LikeRating) shouldBe None
      }

      it("should set and find a rating") {
        val gps = buildPersistenceService()
        gps.setRating(1, 1, LikeRating, 5)
        gps.ratingFor(1, 1, LikeRating).map(_.rating) shouldBe Some(5)
        gps.ratingFor(1, 1, KnowRating) shouldBe None
      }

      it("should set and cancel a rating") {
        val gps = buildPersistenceService()
        gps.setRating(2, 2, LikeRating, 5)
        gps.cancelRating(2, 2, LikeRating)
        gps.ratingFor(2, 2, LikeRating) shouldBe None
      }

      it("should edit a rating") {
        val gps = buildPersistenceService()
        gps.setRating(3, 3, LikeRating, 5)
        gps.setRating(3, 3, LikeRating, 3)
        gps.ratingFor(3, 3, LikeRating).map(_.rating) shouldBe Some(3)
        gps.countRatingsBy(3, LikeRating) shouldBe 1
      }

      it("knows the ratings by a user") {
        val gps = buildPersistenceService()
        gps.setRating(1, 2, LikeRating, 5)
        gps.countRatingsBy(1, KnowRating) shouldBe 0
        gps.countRatingsBy(1, LikeRating) shouldBe 1
      }

      it("returns the titles known by a user") {
        val gps = buildPersistenceService()
        gps.setRating(1, 10, KnowRating, 3)
        gps.setRating(1, 10, LikeRating, 3)
        gps.setRating(1, 11, KnowRating, 4)
        gps.setRating(2, 20, KnowRating, 5)
        gps.knownTitlesFor(1) shouldBe Set(10, 11)
        gps.knownTitlesFor(2) shouldBe Set(20)
      }

      it("does not list any ratings") {
        val gps = buildPersistenceService()
        gps.ratingsFor(1) shouldBe Seq.empty
      }

      it("lists one rating") {
        val gps = buildPersistenceService()
        gps.setRating(1, 10, LikeRating, 3)
        gps.ratingsFor(1) shouldBe Seq(Rating(10, 3, LikeRating, now))
      }
    }

    describe("Videos") {
      describe("by title") {
        it("should find an empty list of videos") {
          val gps = buildPersistenceService()
          gps.videosForTitle(1) shouldBe Seq.empty
        }

        it("should add and find a video") {
          val gps = buildPersistenceService()
          gps.saveVideo(1, 2, VideoInput("abcd"))
          val videos = gps.videosForTitle(1)
          videos.map(_.videoId) shouldBe Seq("abcd")
        }

        it("shouldBe idempotent") {
          val gps = buildPersistenceService()
          gps.saveVideo(2, 3, VideoInput("abcd"))
          gps.saveVideo(2, 3, VideoInput("abcd"))
          val videos = gps.videosForTitle(2)
          videos.map(_.videoId) shouldBe Seq("abcd")
          videos.map(_.userId) shouldBe Seq(3)
        }
      }

      describe("by user") {
        it("finds an empty list") {
          val gps = buildPersistenceService()
          gps.videosForUser(1) shouldBe Seq.empty
        }

        it("should add and find a video") {
          val gps = buildPersistenceService()
          gps.saveVideo(1, 2, VideoInput("abcd"))
          val videos = gps.videosForUser(2)
          videos.map(_.videoId) shouldBe Seq("abcd")
        }

        it("shouldBe idempotent") {
          val gps = buildPersistenceService()
          gps.saveVideo(2, 2, VideoInput("abcd"))
          gps.saveVideo(2, 2, VideoInput("abcd"))
          val videos = gps.videosForUser(2)
          videos.map(_.videoId) shouldBe Seq("abcd")
        }
      }
    }
  }
  
  private def buildPersistenceService(): GeneralPersistenceService = {
    new TestGeneralPersistenceService(new FixedNowProvider(now))
  } 
}
