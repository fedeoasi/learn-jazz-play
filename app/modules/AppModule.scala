package modules

import com.google.inject.AbstractModule
import persistence.SQLiteDatabaseInitializer
import play.libs.akka.AkkaGuiceSupport
import realtime.NotificationsProtocol
import securesocial.MyRuntimeEnvironment
import titles.{TitleRepository, TitleRepositoryProvider}

class AppModule extends AbstractModule with AkkaGuiceSupport {
  private val database = SQLiteDatabaseInitializer.database("courses")

  import scala.slick.driver.JdbcDriver.simple._

  override def configure(): Unit = {
    bindActor(classOf[NotificationsProtocol], "notifications")
    bind(classOf[Database]).toInstance(database)
    bind(classOf[MyRuntimeEnvironment]).toInstance(new MyRuntimeEnvironment(database))
    bind(classOf[TitleRepository]).toProvider(new TitleRepositoryProvider)
  }
}
