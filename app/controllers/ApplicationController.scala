package controllers

import com.google.inject.{Inject, Singleton}
import play.api.mvc._
import securesocial.core._
import service.User

@Singleton
class ApplicationController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends securesocial.core.SecureSocial[User] {
  def index = SecuredAction { implicit request =>
    Ok(views.html.index(request.user.main))
  }

  def home = SecuredAction { implicit request =>
    Ok(views.html.home())
  }

  /**
   * Sample use of SecureSocial.currentUser. Access the /current-user to test it
   */
  def currentUser = Action.async { implicit request =>
    SecureSocial.currentUser[User].map { maybeUser =>
      val userId = maybeUser.map(_.main.userId).getOrElse("unknown")
      Ok(s"Your id is $userId")
    }
  }
}
