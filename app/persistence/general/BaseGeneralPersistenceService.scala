package persistence.general

import model.{Video, VideoInput}
import persistence.Daos._
import play.api.Logger
import time.NowProvider

abstract class BaseGeneralPersistenceService extends GeneralPersistenceService {
  val dal: LearnJazzDAL
  import dal._
  import driver.simple._

  val database: Database

  val nowProvider: NowProvider

  protected val logger = Logger.logger

  override def ratingFor(userId: Int, titleId: Int, ratingType: RatingType): Option[Rating] = {
    database withSession { implicit s =>
      findRating(userId, titleId, ratingType.discriminator).list.map(ratingFromDao).headOption
    }
  }

  private def ratingFromDao(dao: RatingDao): Rating = {
    Rating(dao.titleId, dao.rating, dao.modifiedTime)
  }

  override def ratingsFor(userId: Int): Seq[Rating] = {
    database withSession { implicit s =>
      ratings.filter(_.userId === userId).list.map(ratingFromDao)
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
          val dao = RatingDao(userId, titleId, ratingType.discriminator, rating, nowProvider.now)
          ratings.insert(dao)
      }
    }
  }

  override def knownTitlesFor(userId: Int): Set[Int] = {
    database withSession { implicit s =>
      ratings.filter(_.userId === userId).map(_.titleId).run.toSet
    }
  }

  private def findRating(userId: Column[Int], titleId: Column[Int], ratingType: Column[String]) = ratings.filter {
    r => r.userId === userId && r.titleId === titleId && r.ratingType === ratingType
  }


  override def videosForTitle(titleId: Int): Seq[Video] = {
    database withSession { implicit s =>
      videos.filter(_.titleId === titleId).list.map(videoFromDao)
    }
  }

  override def videosForUser(userId: Int): Seq[Video] = {
    database withSession { implicit s =>
      videos.filter(_.userId === userId).list.map(videoFromDao)
    }
  }

  private def videoFromDao(dao: VideoDao): Video = {
    Video(dao.videoId, dao.userId, dao.modifiedTime, dao.id)
  }

  override def saveVideo(titleId: Int, userId: Int, video: VideoInput): Unit = {
    database withTransaction { implicit s =>
      val existingQuery = videos.filter(v => v.titleId === titleId && v.videoId === video.videoId)
      if (existingQuery.map(_.id).list.headOption.isEmpty) {
        val dao = VideoDao(titleId, userId, video.videoId, nowProvider.now)
        videos.insert(dao)
      }
    }
  }
}

