package persistence.general

import model.Video
import play.api.Logger

import scala.slick.jdbc.meta.MTable

abstract class BaseGeneralPersistenceService extends GeneralPersistenceService {
  val dal: LearnJazzDAL
  import dal._
  import driver.simple._

  val database: Database

  protected val logger = Logger.logger

  override def ratingFor(userId: Int, titleId: Int, ratingType: RatingType): Option[Double] = {
    database withSession { implicit s =>
      findRating(userId, titleId, ratingType.discriminator).map(_.rating).list.headOption
    }
  }

  override def cancelRating(userId: Int, titleId: Int, ratingType: RatingType): Unit = {
    database withSession { implicit s =>
      findRating(userId, titleId, ratingType.discriminator).delete
    }
  }

  override def countRatingsBy(userId: Int, ratingType: RatingType): Int = {
    database withSession { implicit s =>
      ratings
        .filter(r => r.userId === userId && r.ratingType === ratingType.discriminator)
        .list.size
    }
  }

  override def setRating(userId: Int, titleId: Int, ratingType: RatingType, rating: Double): Unit = {
    database withSession { implicit s =>
      val existingRating = findRating(userId, titleId, ratingType.discriminator)
      existingRating.list.headOption match {
        case Some(existingDao) =>
          val dao = existingDao.copy(rating = rating)
          existingRating.update(dao)
        case None =>
          val dao = RatingDao(userId, titleId, ratingType.discriminator, rating)
          ratings.insert(dao)
      }
    }
  }

  private def findRating(userId: Column[Int], titleId: Column[Int], ratingType: Column[String]) = ratings.filter {
    r => r.userId === userId && r.titleId === titleId && r.ratingType === ratingType
  }


  override def videosFor(titleId: Int): Seq[Video] = {
    database withSession { implicit s =>
      videos.filter(_.titleId === titleId).list.map { dao =>
        Video(dao.videoId, dao.id)
      }
    }
  }

  override def saveVideo(titleId: Int, userId: Int, video: Video): Unit = {
    database withTransaction { implicit s =>
      val existingQuery = videos.filter(v => v.titleId === titleId && v.videoId === video.videoId)
      if (existingQuery.map(_.id).list.headOption.isEmpty) {
        val dao = VideoDao(titleId, userId, video.videoId)
        videos.insert(dao)
      }
    }
  }

  def initializeDatabase() {
    database withSession { implicit s =>
      if(!MTable.getTables.list.exists(_.name.name == ratings.shaped.value.tableName)) {
        logger.debug(ratings.ddl.createStatements.mkString("\n"))
        ratings.ddl.create
      }
      if(!MTable.getTables.list.exists(_.name.name == videos.shaped.value.tableName)) {
        logger.info(ratings.ddl.createStatements.mkString("\n"))
        videos.ddl.create
      }
    }
    logger.info("The database has been initialized")
  }
}

