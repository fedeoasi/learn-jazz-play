import java.io.{File, InputStreamReader}

import com.google.inject.{AbstractModule, Guice, Provider, TypeLiteral}
import com.typesafe.config.ConfigFactory
import controllers.CustomRoutesService
import persistence.SQLiteDatabaseInitializer
import persistence.auth.{AuthPersistenceServiceImpl, MyUserService}
import play.api._
import securesocial.core.providers.UsernamePasswordProvider
import securesocial.core.services.UserService
import securesocial.core.{IdentityProvider, RuntimeEnvironment}
import service.{MyEventListener, User}
import titles.{InMemoryTitleRepository, TitleRepository}

import scala.collection.immutable.ListMap

object Global extends play.api.GlobalSettings {
  private val database = SQLiteDatabaseInitializer.database("courses")
  private val logger = Logger.logger

  object MyRuntimeEnvironment extends RuntimeEnvironment.Default[User] {
    override implicit val executionContext = play.api.libs.concurrent.Execution.defaultContext
    override lazy val routes = new CustomRoutesService()
    override lazy val userService: UserService[User] = new MyUserService(new AuthPersistenceServiceImpl(database))
    override lazy val eventListeners = List(new MyEventListener())
    override lazy val providers: ListMap[String, IdentityProvider] = ListMap(
      include(new UsernamePasswordProvider[User](userService, avatarService, viewTemplates, passwordHashers))
    )
  }

  import scala.slick.driver.JdbcDriver.simple._

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(new TypeLiteral[RuntimeEnvironment[User]] {}).toInstance(MyRuntimeEnvironment)
      bind(classOf[Database]).toInstance(database)
      bind(classOf[TitleRepository]).toProvider(new Provider[TitleRepository] {
        lazy val titleRepository = {
          val resource = Play.current.classloader.getResourceAsStream("jazz_standards.csv")
          val reader = new InputStreamReader(resource)
          InMemoryTitleRepository(reader)
        }
        override def get(): TitleRepository = titleRepository
      })
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = injector.getInstance(controllerClass)

  override def onLoadConfig(config: Configuration, path: File, classloader: ClassLoader, mode: Mode.Mode): Configuration = {
    val configOverride = s"application.${mode.toString.toLowerCase}.conf"
    logger.info(s"looking for config override at: $configOverride")
    val modeSpecificConfig = config ++ Configuration(ConfigFactory.load(configOverride))
    super.onLoadConfig(modeSpecificConfig, path, classloader, mode)
  }
}