package persistence.general

import org.joda.time.DateTime
import persistence.{Dal, Profile, DBComponent}
import persistence.Daos._

import scala.slick.driver.JdbcDriver

trait GeneralDbComponent extends DBComponent {
  this: Profile =>

  import driver.simple._
  import JodaSupport._

  class Ratings(tag: Tag) extends Table[RatingDao](tag, "RATINGS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc, O.NotNull)
    def userId = column[Int]("USER_ID", O.NotNull)
    def titleId = column[Int]("TITLE_ID")
    def ratingType = column[String]("RATING_TYPE")
    def rating = column[Double]("RATING")
    def modifiedTime = column[DateTime]("MODIFIED_TIME")
    def * = (userId, titleId, ratingType, rating, modifiedTime, id.?) <> (RatingDao.tupled, RatingDao.unapply)

    def ownIdTargetIdx = index("USER_TITLE_IDX", (userId, titleId), unique = false)
  }
  object ratings extends TableQuery(new Ratings(_))

  class Videos(tag: Tag) extends Table[VideoDao](tag, "VIDEOS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc, O.NotNull)
    def userId = column[Int]("USER_ID", O.NotNull)
    def titleId = column[Int]("TITLE_ID")
    def videoId = column[String]("VIDEO_ID")
    def modifiedTime = column[DateTime]("MODIFIED_TIME")
    def * = (titleId, userId, videoId, modifiedTime, id.?) <> (VideoDao.tupled, VideoDao.unapply)

    def ownIdTargetIdx = index("TITLE_IDX", (userId, titleId), unique = false)
  }
  object videos extends TableQuery(new Videos(_))

  val tables = Seq(ratings, videos)
}

class LearnJazzDAL(override val driver: JdbcDriver) extends GeneralDbComponent
  with Dal with Profile {

  import driver.simple._
  override def createStatements: Seq[String] = tables.flatMap(_.ddl.createStatements)
  override def create(implicit session: driver.simple.Session): Unit = tables.foreach(_.ddl.create)
}