package controllers

import com.google.inject.Inject
import play.api.mvc.RequestHeader
import securesocial.controllers._
import securesocial.core.services.RoutesService
import securesocial.core.{IdentityProvider, RuntimeEnvironment}
import service.User

class CustomLoginController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends BaseLoginPage[User] {}

class CustomProviderController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends BaseProviderController[User] { }

class CustomRegistrationController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends BaseRegistration[User] {}

class CustomPasswordResetController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends BasePasswordReset[User] {}

class CustomPasswordChangeController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends BasePasswordChange[User] {}

class CustomLoginApiController @Inject() (override implicit val env: RuntimeEnvironment[User]) extends BaseLoginApi[User] {}

class CustomRoutesService extends RoutesService.Default {
  override def loginPageUrl(implicit req: RequestHeader): String = {
    controllers.routes.CustomLoginController.login().absoluteURL(IdentityProvider.sslEnabled)
  }
}
