package persistence

import java.io.File
import java.util.UUID

import model.RatingTypes._
import org.scalatest.{BeforeAndAfterAll, Matchers, FunSpec}
import persistence.auth.AuthPersistenceServiceImpl
import persistence.general.GeneralPersistenceServiceImpl

class DatabaseMigrationSpec extends FunSpec with Matchers with BeforeAndAfterAll {
  private val dbName = s"testDb${UUID.randomUUID()}"
  private val db = SQLiteDatabaseInitializer.database(dbName)

  describe("Database Migration Step") {
    it("supports the auth model") {
      val ps = new AuthPersistenceServiceImpl(db)
      ps.findUserByEmail("hello@gmail.com") shouldBe None
    }

    it("supports the general model") {
      val ps = new GeneralPersistenceServiceImpl(db)
      ps.ratingFor(1, 1, LikeRating) shouldBe None
    }
  }

  override protected def afterAll(): Unit = {
    new File(s"$dbName.db").delete()
  }
}
