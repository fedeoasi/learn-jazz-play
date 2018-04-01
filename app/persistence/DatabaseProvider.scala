package persistence

import com.google.inject.{Inject, Provider}
import play.api.Configuration

import scala.slick.driver.SQLiteDriver.simple._

class DatabaseProvider @Inject() (configuration: Configuration) extends Provider[Database] {
  override def get(): Database = {
    configuration.getString("database.url") match {
      case Some(url) => PostgresDatabaseInitializer.init(url)
      case None => SQLiteDatabaseInitializer.database("courses")
    }
  }
}
