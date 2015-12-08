package securesocial

import _root_.controllers.CustomRoutesService
import persistence.auth.{AuthPersistenceServiceImpl, MyUserService}
import securesocial.core.providers.UsernamePasswordProvider
import securesocial.core.{IdentityProvider, RuntimeEnvironment}
import securesocial.core.services.UserService
import service.{MyEventListener, User}
import scala.slick.driver.JdbcDriver.simple._

import scala.collection.immutable.ListMap

class MyRuntimeEnvironment(database: Database) extends RuntimeEnvironment.Default {
  override type U = User
  override implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext
  override lazy val routes = new CustomRoutesService()
  override lazy val userService: UserService[User] = new MyUserService(new AuthPersistenceServiceImpl(database))
  override lazy val eventListeners = List(new MyEventListener())
  override lazy val providers: ListMap[String, IdentityProvider] = ListMap(
    include(new UsernamePasswordProvider[User](userService, avatarService, viewTemplates, passwordHashers))
  )
}