package persistence.general

import com.google.inject.Inject
import scala.slick.driver.SQLiteDriver
import scala.slick.driver.SQLiteDriver.simple._

class GeneralPersistenceServiceImpl @Inject() (override val database: Database) extends BaseGeneralPersistenceService {
  override val dal: LearnJazzDAL = new LearnJazzDAL(SQLiteDriver)
}
