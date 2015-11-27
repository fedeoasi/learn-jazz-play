package service

import model.{Title, VideoInput}
import org.joda.time.DateTime
import org.scalatest.{FunSpec, Matchers}
import persistence.auth.AuthSpecHelper._
import persistence.general.{GeneralPersistenceService, LikeRating, TestGeneralPersistenceService}
import securesocial.core.AuthenticationMethod
import time.FixedNowProvider
import titles.{InMemoryTitleDataSource, TitleDataSource}

class ActivityServiceSpec extends FunSpec with Matchers {
  private val baseProfile = buildProfile("pid", "uid", AuthenticationMethod.UserPassword)
  private val user = User(baseProfile, List(baseProfile), 1)
  private val title = Title(10, "Ten", 2010, 3)
  private val titleDataSource = buildTitleDataSource()
  private val now = DateTime.now

  it("returns an empty activity list") {
    val gps = buildPersistenceService()
    val service = new ActivityServiceImpl(titleDataSource, gps)
    service.activityFor(user) shouldBe Seq.empty
  }

  it("returns a rating event") {
    val gps = buildPersistenceService()
    gps.setRating(1, 10, LikeRating, 3)
    val service = new ActivityServiceImpl(titleDataSource, gps)
    val expected = Seq(RatedTitle(user, now, title))
    service.activityFor(user) shouldBe expected
  }

  it("returns a video event") {
    val gps = buildPersistenceService()
    gps.saveVideo(10, 1, VideoInput("asdf"))
    val savedVideo = gps.videosForUser(1).head
    val service = new ActivityServiceImpl(titleDataSource, gps)
    val expected = Seq(EnteredVideo(user, now, savedVideo))
    service.activityFor(user) shouldBe expected
  }

  private def buildTitleDataSource(): TitleDataSource = {
    new InMemoryTitleDataSource(Seq(title))
  }

  private def buildPersistenceService(): GeneralPersistenceService = {
    new TestGeneralPersistenceService(new FixedNowProvider(now))
  }
}
