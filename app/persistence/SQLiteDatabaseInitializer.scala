package persistence

import org.sqlite.SQLiteDataSource
import scala.slick.driver.SQLiteDriver.simple._

object SQLiteDatabaseInitializer {
  def database(dbName: String) = {
    val url = "jdbc:sqlite:%s.db" format dbName
    val ds = new SQLiteDataSource()
    ds.setUrl(url)
    val db = Database.forDataSource(ds)
    db
  }
}