package controllers

import com.google.inject.{Inject, Singleton}
import securesocial.CustomRuntimeEnvironment

@Singleton
class ApplicationController @Inject() (override implicit val env: CustomRuntimeEnvironment)
  extends securesocial.core.SecureSocial {

  def index = SecuredAction { implicit request =>
    Ok(views.html.index(request.user.main))
  }

  def home = SecuredAction { implicit request =>
    Ok(views.html.home())
  }
}
