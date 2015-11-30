package service

import model.{EnteredVideo, RatedTitle, VideoInput}
import org.scalatest.{FunSpec, Matchers}
import persistence.PersistenceSpecHelper._
import persistence.auth.AuthSpecHelper._
import persistence.general.{KnowRating, LikeRating}
import titles.TitleSpecHelper._

class ActivityServiceSpec extends FunSpec with Matchers {
  describe("Activity") {
    it("returns an empty activity list") {
      val gps = buildPersistenceService()
      val service = new ActivityServiceImpl(titleRepository, gps)
      service.activityFor(user) shouldBe Seq.empty
    }

    it("returns a rating event") {
      val gps = buildPersistenceService()
      gps.setRating(1, 10, LikeRating, 3)
      val service = new ActivityServiceImpl(titleRepository, gps)
      val expected = Seq(RatedTitle(now, title))
      service.activityFor(user) shouldBe expected
    }

    it("returns a video event") {
      val gps = buildPersistenceService()
      gps.saveVideo(10, 1, VideoInput("asdf"))
      val savedVideo = gps.videosForUser(1).head
      val service = new ActivityServiceImpl(titleRepository, gps)
      val expected = Seq(EnteredVideo(now, savedVideo))
      service.activityFor(user) shouldBe expected
    }
  }

  describe("User Stats") {
    it("returns empty stats for a user") {
      val gps = buildPersistenceService()
      val service = new ActivityServiceImpl(titleRepository, gps)
      service.userStatsFor(user) shouldBe UserStats(user.id, 0, 0)
    }

    it("counts the number of ratings") {
      val gps = buildPersistenceService()
      val service = new ActivityServiceImpl(titleRepository, gps)
      gps.setRating(user.id, title.id, LikeRating, 2)
      gps.setRating(user.id, title2.id, KnowRating, 3)
      service.userStatsFor(user) shouldBe UserStats(user.id, 2, 0)
    }


    it("counts the number of added videos") {
      val gps = buildPersistenceService()
      val service = new ActivityServiceImpl(titleRepository, gps)
      gps.saveVideo(title2.id, user.id, VideoInput("asdf"))
      service.userStatsFor(user) shouldBe UserStats(user.id, 0, 1)
    }
  }
}
