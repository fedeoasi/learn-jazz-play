import java.io.File

import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.typesafe.config.ConfigFactory
import controllers.CustomRoutesService
import persistence.{MyUserService, ProdAuthPersistenceService}
import play.api._
import securesocial.core.providers.UsernamePasswordProvider
import securesocial.core.{IdentityProvider, RuntimeEnvironment}
import securesocial.core.services.UserService
import service.{MyEventListener, User}

import scala.collection.immutable.ListMap

object Global extends play.api.GlobalSettings {
  object MyRuntimeEnvironment extends RuntimeEnvironment.Default[User] {
    override implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext
    override lazy val routes = new CustomRoutesService()
    override lazy val userService: UserService[User] = new MyUserService(new ProdAuthPersistenceService("courses"))
    override lazy val eventListeners = List(new MyEventListener())
    override lazy val providers: ListMap[String, IdentityProvider] = ListMap(
      include(new UsernamePasswordProvider[User](userService, avatarService, viewTemplates, passwordHashers))
    )
  }

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(new TypeLiteral[RuntimeEnvironment[User]] {}).toInstance(MyRuntimeEnvironment)
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val modeSpecificConfig = config ++ Configuration(ConfigFactory.load(s"application.${mode.toString.toLowerCase}.conf"))
    super.onLoadConfig(modeSpecificConfig, path, classloader, mode)
  }
}