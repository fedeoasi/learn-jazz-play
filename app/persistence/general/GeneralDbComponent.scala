package persistence.general

import persistence.{Profile, DBComponent}

import scala.slick.driver.JdbcDriver

trait GeneralDbComponent extends DBComponent {
  this: Profile =>

  import driver.simple._

  class Ratings(tag: Tag) extends Table[RatingDao](tag, "RATINGS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc, O.NotNull)
    def userId = column[Int]("USER_ID", O.NotNull)
    def titleId = column[Int]("TITLE_ID")
    def ratingType = column[String]("RATING_TYPE")
    def rating = column[Double]("RATING")
    def * = (userId, titleId, ratingType, rating, id.?) <> (RatingDao.tupled, RatingDao.unapply)

    def ownIdTargetIdx = index("USER_TITLE_IDX", (userId, titleId), unique = false)
  }
  object ratings extends TableQuery(new Ratings(_))

  class Videos(tag: Tag) extends Table[VideoDao](tag, "VIDEOS") {
    def id = column[Int]("ID", O.PrimaryKey, O.AutoInc, O.NotNull)
    def userId = column[Int]("USER_ID", O.NotNull)
    def titleId = column[Int]("TITLE_ID")
    def videoId = column[String]("VIDEO_ID")
    def * = (titleId, userId, videoId, id.?) <> (VideoDao.tupled, VideoDao.unapply)

    def ownIdTargetIdx = index("TITLE_IDX", (userId, titleId), unique = false)
  }
  object videos extends TableQuery(new Videos(_))
}

class LearnJazzDAL(override val driver: JdbcDriver) extends GeneralDbComponent with Profile

case class RatingDao(userId: Int, titleId: Int, ratingType: String, rating: Double, id: Option[Int] = None)
case class VideoDao(titleId: Int, userId: Int, videoId: String, id: Option[Int] = None)
