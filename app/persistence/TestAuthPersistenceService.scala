package persistence

import java.util.UUID

import scala.slick.driver.H2Driver
import scala.slick.driver.SQLiteDriver.simple._

class TestAuthPersistenceService extends AuthPersistenceService {
  val database = Database.forURL(s"jdbc:h2:mem:userapp" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1",
    driver = "org.h2.Driver")
  override val dal: AuthDal = new AuthDal(H2Driver)

  initializeDatabase()
}
