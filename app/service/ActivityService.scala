package service

import com.google.inject.{Inject, ImplementedBy}
import persistence.general.GeneralPersistenceService
import titles.TitleDataSource

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
      RatedTitle(user, r.modifiedDate, titleDataSource(r.titleId))
    }
    ratingEvents
  }
}

