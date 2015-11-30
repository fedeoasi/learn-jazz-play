package persistence

import org.joda.time.DateTime
import persistence.general.{GeneralPersistenceService, TestGeneralPersistenceService}
import time.FixedNowProvider

object PersistenceSpecHelper {
  val now = DateTime.now

  def buildPersistenceService(): GeneralPersistenceService = {
    new TestGeneralPersistenceService(new FixedNowProvider(now))
  }
}
