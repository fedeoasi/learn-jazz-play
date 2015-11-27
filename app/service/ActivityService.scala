package service

import com.google.inject.{Inject, ImplementedBy}
import persistence.general.GeneralPersistenceService
import titles.TitleDataSource
import com.github.nscala_time.time.Imports._

@ImplementedBy(classOf[ActivityServiceImpl])
trait ActivityService {
  def activityFor(user: User): Seq[Activity]
}

class ActivityServiceImpl @Inject() (titleDataSource: TitleDataSource,
                                     generalPersistenceService: GeneralPersistenceService)
  extends ActivityService {

  override def activityFor(user: User): Seq[Activity] = {
    val ratings = generalPersistenceService.ratingsFor(user.id)
    val ratingEvents = ratings.map { r =>
      RatedTitle(r.modifiedDate, titleDataSource(r.titleId))
    }
    val videos = generalPersistenceService.videosForUser(user.id)
    val videoEvents = videos.map { v =>
      EnteredVideo(v.modifiedTime, v)
    }
    val allEvents = ratingEvents ++ videoEvents
    allEvents.sortBy(_.timestamp).reverse
  }
}

