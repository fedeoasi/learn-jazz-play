package persistence.general

import java.util.UUID

import org.h2.jdbcx.JdbcDataSource
import scala.slick.driver.H2Driver
import scala.slick.driver.H2Driver.simple._

class TestGeneralPersistenceService extends BaseGeneralPersistenceService {
  val database = createTestDatabase()
  override val dal: LearnJazzDAL = new LearnJazzDAL(H2Driver)

  def createTestDatabase(): Database = {
    val ds = new JdbcDataSource()
    ds.setURL(s"jdbc:h2:mem:userapp" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1")
    Database.forDataSource(ds)
  }

  initializeDatabase()
}