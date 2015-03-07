package persistence

import scala.slick.driver.SQLiteDriver.simple._
import scala.slick.driver.SQLiteDriver

class ProdAuthPersistenceService(dbName: String) extends AuthPersistenceService {
  val database = Database.forURL(
    "jdbc:sqlite:%s.db" format dbName,
    driver = "org.sqlite.JDBC")
  override val dal: AuthDal = new AuthDal(SQLiteDriver)

  initializeDatabase()
}
