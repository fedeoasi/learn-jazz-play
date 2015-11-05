package controllers

import com.google.inject.Inject
import model.{VideoInput, Video}
import persistence.general.GeneralPersistenceService
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.data.Forms._
import play.utils.UriEncoding
import securesocial.core.{RuntimeEnvironment, SecureSocial}
import serialization.VideosSerializer
import service.User
import video.YoutubeResolver

class VideosController @Inject() (generalPersistenceService: GeneralPersistenceService,
                                 override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  private val logger = Logger.logger
  private val videoSerializer = new VideosSerializer
  private val videoForm = Form(
    mapping("url" -> text)
    (VideoFormData.apply)(VideoFormData.unapply)
  )
  private val youtubeResolver = new YoutubeResolver

  def get(titleId: Int) = SecuredAction { implicit r =>
    val videos = generalPersistenceService.videosForTitle(titleId)
    Ok(videoSerializer.serializeMany(videos))
  }

  def post(titleId: Int) = SecuredAction { implicit r =>
    val boundForm = videoForm.bindFromRequest()
    val url = boundForm.get.url
    val decoded = UriEncoding.decodePath(url, "UTF-8")
    youtubeResolver.extractVideoId(decoded) match {
      case Some(id) =>
        generalPersistenceService.saveVideo(titleId, 1, VideoInput(id))
        Ok("Saved video")
      case None =>
        BadRequest("Could not extract youtube id")
    }
  }

  def resolveLink(url: String) = SecuredAction { implicit r =>
    logger.info(s"resolving link at url $url")
    youtubeResolver.extractVideoId(url) match {
      case Some(id) => Ok(youtubeResolver.resolveUrl(url))
      case None => BadRequest("Only Youtube link resolution is supported")
    }
  }
}

case class VideosResponse(videos: Seq[Video])
case class VideoFormData(url: String)