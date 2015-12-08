package controllers

import com.google.inject.Inject
import play.api.mvc.RequestHeader
import securesocial.MyRuntimeEnvironment
import securesocial.controllers._
import securesocial.core.services.RoutesService
import securesocial.core.{IdentityProvider, RuntimeEnvironment}
import service.User

class CustomLoginController @Inject() (override implicit val env: MyRuntimeEnvironment) extends BaseLoginPage

class CustomProviderController @Inject() (override implicit val env: MyRuntimeEnvironment) extends BaseProviderController

class CustomRegistrationController @Inject() (override implicit val env: MyRuntimeEnvironment) extends BaseRegistration

class CustomPasswordResetController @Inject() (override implicit val env: MyRuntimeEnvironment) extends BasePasswordReset

class CustomPasswordChangeController @Inject() (override implicit val env: MyRuntimeEnvironment) extends BasePasswordChange

class CustomLoginApiController @Inject() (override implicit val env: MyRuntimeEnvironment) extends BaseLoginApi

class CustomRoutesService extends RoutesService.Default {
  override def loginPageUrl(implicit req: RequestHeader): String = {
    controllers.routes.CustomLoginController.login().absoluteURL(IdentityProvider.sslEnabled)
  }
}
