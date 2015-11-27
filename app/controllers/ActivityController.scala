package controllers

import com.google.inject.Inject
import securesocial.core.{RuntimeEnvironment, SecureSocial}
import serialization.BaseActivitySerializer
import service.{ActivityService, User}

class ActivityController @Inject() (activityService: ActivityService,
                                    override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  val serializer = new BaseActivitySerializer

  def get() = SecuredAction { implicit r =>
    val activities = activityService.activityFor(r.user)
    Ok(serializer.serializeMany(activities.map(_.toBaseActivity)))
  }
}
