package persistence.general

import java.util.UUID

import org.h2.jdbcx.JdbcDataSource
import scala.slick.driver.H2Driver

class TestGeneralPersistenceService extends BaseGeneralPersistenceService {
  override val dal: LearnJazzDAL = new LearnJazzDAL(H2Driver)
  import dal._
  import driver.simple._

  val database = createTestDatabase()

  def createTestDatabase(): Database = {
    val ds = new JdbcDataSource()
    ds.setURL(s"jdbc:h2:mem:userapp" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1")
    Database.forDataSource(ds)
  }

  def initializeDatabase() {
    database withSession { implicit s =>
      dal.tables.foreach(_.ddl.create)
    }
  }

  initializeDatabase()
}