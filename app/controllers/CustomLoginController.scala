package controllers

import com.google.inject.Inject
import play.api.mvc.RequestHeader
import play.filters.csrf.{CSRFAddToken, CSRFCheck}
import securesocial.CustomRuntimeEnvironment
import securesocial.controllers._
import securesocial.core.IdentityProvider
import securesocial.core.services.RoutesService

class CustomLoginController @Inject() (val csrfAddToken: CSRFAddToken, override implicit val env: CustomRuntimeEnvironment) extends BaseLoginPage

class CustomProviderController @Inject() (override implicit val env: CustomRuntimeEnvironment) extends BaseProviderController

class CustomRegistrationController @Inject() (val csrfAddToken: CSRFAddToken, val csrfCheck: CSRFCheck, override implicit val env: CustomRuntimeEnvironment) extends BaseRegistration

class CustomPasswordResetController @Inject() (val csrfAddToken: CSRFAddToken, val csrfCheck: CSRFCheck, override implicit val env: CustomRuntimeEnvironment) extends BasePasswordReset

class CustomPasswordChangeController @Inject() (val csrfAddToken: CSRFAddToken, val csrfCheck: CSRFCheck, override implicit val env: CustomRuntimeEnvironment) extends BasePasswordChange

class CustomLoginApiController @Inject() (override implicit val env: CustomRuntimeEnvironment) extends BaseLoginApi

class CustomRoutesService extends RoutesService.Default {
  override def loginPageUrl(implicit req: RequestHeader): String = {
    controllers.routes.CustomLoginController.login().absoluteURL(IdentityProvider.sslEnabled)
  }
}
