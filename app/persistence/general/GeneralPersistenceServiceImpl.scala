package persistence.general

import com.google.inject.Inject
import time.{NowProviderImpl, NowProvider}
import scala.slick.driver.SQLiteDriver
import scala.slick.driver.SQLiteDriver.simple._

class GeneralPersistenceServiceImpl @Inject() (override val database: Database,
                                               override val nowProvider: NowProvider = new NowProviderImpl)
  extends BaseGeneralPersistenceService {

  override val dal: LearnJazzDAL = new LearnJazzDAL(SQLiteDriver)
}
