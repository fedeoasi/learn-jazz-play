import java.io.File

import com.google.inject.{AbstractModule, Guice, TypeLiteral}
import com.typesafe.config.ConfigFactory
import persistence.SQLiteDatabaseInitializer
import play.api._
import securesocial.MyRuntimeEnvironment
import securesocial.core.RuntimeEnvironment
import service.User
import titles.{TitleRepository, TitleRepositoryProvider}

object Global extends play.api.GlobalSettings {
  private val database = SQLiteDatabaseInitializer.database("courses")
  private val logger = Logger.logger

  import scala.slick.driver.JdbcDriver.simple._

  val injector = Guice.createInjector(new AbstractModule {
    protected def configure() {
      bind(classOf[Database]).toInstance(database)
      bind(new TypeLiteral[RuntimeEnvironment[User]] {}).toInstance(new MyRuntimeEnvironment(database))
      bind(classOf[TitleRepository]).toProvider(new TitleRepositoryProvider)
    }
  })

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    injector.getInstance(controllerClass)
  }

  override def onLoadConfig(config: Configuration,
                            path: File,
                            classloader: ClassLoader,
                            mode: Mode.Mode): Configuration = {

    val configOverride = s"application.${mode.toString.toLowerCase}.conf"
    logger.info(s"looking for config override at: $configOverride")
    val modeSpecificConfig = config ++ Configuration(ConfigFactory.load(configOverride))
    super.onLoadConfig(modeSpecificConfig, path, classloader, mode)
  }
}