package controllers

import com.google.inject.Inject
import model.Video
import persistence.general.GeneralPersistenceService
import play.api.data.Form
import play.api.data.Forms._
import securesocial.core.{SecureSocial, RuntimeEnvironment}
import serialization.VideosSerializer
import service.User
import video.YoutubeResolver
import play.api.Play.current

class VideosController @Inject() (generalPersistenceService: GeneralPersistenceService,
                                 override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  private val videoSerializer = new VideosSerializer
  private val ratingForm = Form(
    mapping("rating" -> text)
    (VideoFormData.apply)(VideoFormData.unapply)
  )
  private val youtubeResolver = new YoutubeResolver

  def get(titleId: Int) = SecuredAction { implicit r =>
    val videos = generalPersistenceService.videosFor(titleId)
    Ok(videoSerializer.serializeMany(videos))
  }

  def post(titleId: Int) = SecuredAction { implicit r =>
    val boundForm = ratingForm.bindFromRequest()
    val url = boundForm.get.url
    val youtubeId = youtubeResolver.resolveUrl(url)
    generalPersistenceService.saveVideo(titleId, 1, Video(youtubeId))
    Ok("Saved video")
  }
}

case class VideosResponse(videos: Seq[Video])
case class VideoFormData(url: String)