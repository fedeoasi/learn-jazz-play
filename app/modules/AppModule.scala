package modules

import com.google.inject.{AbstractModule, Singleton}
import persistence.DatabaseProvider
import play.libs.akka.AkkaGuiceSupport
import realtime.NotificationsProtocol
import titles.{TitleRepository, TitleRepositoryProvider}

class AppModule extends AbstractModule with AkkaGuiceSupport {
  import scala.slick.driver.JdbcDriver.simple._

  override def configure(): Unit = {
    bindActor(classOf[NotificationsProtocol], "notifications")
    bind(classOf[Database]).toProvider(classOf[DatabaseProvider]).in(classOf[Singleton])
    bind(classOf[TitleRepository]).toProvider(new TitleRepositoryProvider)
  }
}
