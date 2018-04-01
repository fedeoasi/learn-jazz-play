package persistence

import scala.slick.driver.SQLiteDriver.simple._

object PostgresDatabaseInitializer {
  def init(url: String): Database = {
    Database.forURL(url, driver = "org.postgresql.ds.PGSimpleDataSource")
  }
}
