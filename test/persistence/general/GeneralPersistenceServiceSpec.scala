package persistence.general

import model.Video
import org.scalatest.{Matchers, FunSpec}

class GeneralPersistenceServiceSpec extends FunSpec with Matchers {
  describe("General Persistence Service") {
    describe("Ratings") {
      it("should not find a non existing rating") {
        val gps = buildPersistenceService()
        gps.ratingFor(1, 1, LikeRating) should be(None)
      }

      it("should set and find a rating") {
        val gps = buildPersistenceService()
        gps.setRating(1, 1, LikeRating, 5)
        gps.ratingFor(1, 1, LikeRating) should be(Some(5))
        gps.ratingFor(1, 1, KnowRating) should be(None)
      }

      it("should set and cancel a rating") {
        val gps = buildPersistenceService()
        gps.setRating(2, 2, LikeRating, 5)
        gps.cancelRating(2, 2, LikeRating)
        gps.ratingFor(2, 2, LikeRating) should be(None)
      }

      it("should edit a rating") {
        val gps = buildPersistenceService()
        gps.setRating(3, 3, LikeRating, 5)
        gps.setRating(3, 3, LikeRating, 3)
        gps.ratingFor(3, 3, LikeRating) should be(Some(3))
        gps.countRatingsBy(3, LikeRating) should be(1)
      }

      it("knows the ratings by a user") {
        val gps = buildPersistenceService()
        gps.setRating(1, 2, LikeRating, 5)
        gps.countRatingsBy(1, KnowRating) should be(0)
        gps.countRatingsBy(1, LikeRating) should be(1)
      }
    }

    describe("Videos") {
      it("should find an empty list of videos") {
        val gps = buildPersistenceService()
        gps.videosFor(1) should be(Seq.empty)
      }

      it("should add and find a video") {
        val gps = buildPersistenceService()
        gps.saveVideo(1, 2, Video("abcd"))
        val videos = gps.videosFor(1)
        videos.size should be(1)
        videos should contain (Video("abcd", Some(1)))
      }

      it("should be idempotent") {
        val gps = buildPersistenceService()
        gps.saveVideo(2, 2, Video("abcd"))
        gps.saveVideo(2, 2, Video("abcd"))
        val videos = gps.videosFor(2)
        videos.size should be(1)
        videos should contain (Video("abcd", Some(1)))
      }
    }
  }
  
  private def buildPersistenceService(): GeneralPersistenceService = {
    new TestGeneralPersistenceService()
  } 
}