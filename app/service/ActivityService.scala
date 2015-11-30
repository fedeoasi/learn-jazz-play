package service

import com.google.inject.{Inject, ImplementedBy}
import model.{EnteredVideo, RatedTitle, Activity}
import persistence.general.GeneralPersistenceService
import titles.TitleRepository
import com.github.nscala_time.time.Imports._

@ImplementedBy(classOf[ActivityServiceImpl])
trait ActivityService {
  def activityFor(user: User): Seq[Activity]
  def userStatsFor(user: User): UserStats
}

class ActivityServiceImpl @Inject() (titleRepository: TitleRepository,
                                     generalPersistenceService: GeneralPersistenceService)
  extends ActivityService {

  override def activityFor(user: User): Seq[Activity] = {
    val ratings = generalPersistenceService.ratingsFor(user.id)
    val ratingEvents = ratings.map { r =>
      RatedTitle(r.modifiedDate, titleRepository(r.titleId))
    }
    val videos = generalPersistenceService.videosForUser(user.id)
    val videoEvents = videos.map { v =>
      EnteredVideo(v.modifiedTime, v)
    }
    val allEvents = ratingEvents ++ videoEvents
    allEvents.sortBy(_.timestamp).reverse
  }

  override def userStatsFor(user: User): UserStats = {
    val ratedTitleCount = generalPersistenceService.knownTitlesFor(user.id).size
    val enteredVideoCount = generalPersistenceService.videosForUser(user.id).size
    UserStats(user.id, ratedTitleCount, enteredVideoCount)
  }
}

