package securesocial

import _root_.controllers.CustomRoutesService
import persistence.auth.{AuthPersistenceServiceImpl, CustomUserService}
import securesocial.core.providers.UsernamePasswordProvider
import securesocial.core.{IdentityProvider, RuntimeEnvironment}
import securesocial.core.services.UserService
import service.{CustomEventListener, User}
import scala.slick.driver.JdbcDriver.simple._

import scala.collection.immutable.ListMap

class CustomRuntimeEnvironment(database: Database) extends RuntimeEnvironment.Default {
  override type U = User
  override implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext
  override lazy val routes = new CustomRoutesService()
  override lazy val userService: UserService[User] = new CustomUserService(new AuthPersistenceServiceImpl(database))
  override lazy val eventListeners = List(new CustomEventListener())
  override lazy val providers: ListMap[String, IdentityProvider] = ListMap(
    include(new UsernamePasswordProvider[User](userService, avatarService, viewTemplates, passwordHashers))
  )
}