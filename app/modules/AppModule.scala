package modules

import com.google.inject.AbstractModule
import persistence.SQLiteDatabaseInitializer
import securesocial.MyRuntimeEnvironment
import titles.{TitleRepositoryProvider, TitleRepository}

class AppModule extends AbstractModule {
  private val database = SQLiteDatabaseInitializer.database("courses")

  import scala.slick.driver.JdbcDriver.simple._

  override def configure(): Unit = {
    bind(classOf[Database]).toInstance(database)
    bind(classOf[MyRuntimeEnvironment]).toInstance(new MyRuntimeEnvironment(database))
    bind(classOf[TitleRepository]).toProvider(new TitleRepositoryProvider)
  }
}
