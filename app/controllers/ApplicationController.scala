package controllers

import com.google.inject.{Inject, Singleton}
import play.api.mvc._
import securesocial.MyRuntimeEnvironment
import securesocial.core._
import service.User

@Singleton
class ApplicationController @Inject() (override implicit val env: MyRuntimeEnvironment)
  extends securesocial.core.SecureSocial {

  def index = SecuredAction { implicit request =>
    Ok(views.html.index(request.user.main))
  }

  def home = SecuredAction { implicit request =>
    Ok(views.html.home())
  }
}
