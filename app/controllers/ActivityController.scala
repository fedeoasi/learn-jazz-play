package controllers

import com.google.inject.Inject
import securesocial.core.{RuntimeEnvironment, SecureSocial}
import serialization.{UserStatsSerializer, BaseActivitySerializer}
import service.{ActivityService, User}

class ActivityController @Inject() (activityService: ActivityService,
                                    override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  val activitySerializer = new BaseActivitySerializer
  val statsSerializer = new UserStatsSerializer


  def listActivity() = SecuredAction { implicit r =>
    val activities = activityService.activityFor(r.user).take(10)
    Ok(activitySerializer.serializeMany(activities.map(_.toBaseActivity)))
  }

  def userStats() = SecuredAction { implicit r =>
    val stats = activityService.userStatsFor(r.user)
    Ok(statsSerializer.serialize(stats))
  }
}
