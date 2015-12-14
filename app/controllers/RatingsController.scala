package controllers

import akka.actor.ActorRef
import com.google.inject.Inject
import com.google.inject.name.Named
import links.Link
import model.RatingType
import model.RatingTypes._
import persistence.general.GeneralPersistenceService
import play.api.data.Form
import play.api.data.Forms._
import realtime.NotificationsActor.SocketMessage
import securesocial.MyRuntimeEnvironment
import securesocial.core.SecureSocial
import serialization.TitleWithRatingSerializer
import service.RatingService
import titles.TitleRepository

class RatingsController @Inject() (generalPersistenceService: GeneralPersistenceService,
                                   ratingService: RatingService,
                                   titleRepository: TitleRepository,
                                   @Named("notifications") notificationsActor: ActorRef,
                                   override implicit val env: MyRuntimeEnvironment)
  extends SecureSocial {

  case class RatingFormData(rating: String)

  private val ratingForm = Form(
    mapping("rating" -> text)
    (RatingFormData.apply)
    (RatingFormData.unapply)
  )

  private val serializer = new TitleWithRatingSerializer

  def know(titleId: Int) = SecuredAction { implicit r =>
    get(titleId, KnowRating)
  }

  def postKnow(titleId: Int) = SecuredAction { implicit r =>
    post(titleId, KnowRating)
  }

  def cancelKnow(titleId: Int) = SecuredAction { implicit r =>
    cancel(titleId, KnowRating)
  }

  def like(titleId: Int) = SecuredAction { implicit r =>
    get(titleId, LikeRating)
  }

  def postLike(titleId: Int) = SecuredAction { implicit r =>
    post(titleId, LikeRating)
  }

  def cancelLike(titleId: Int) = SecuredAction { implicit r =>
    cancel(titleId, LikeRating)
  }

  def favoriteTitleRatings = SecuredAction { implicit r =>
    Ok(serializer.serializeMany(ratingService.favoriteTitles(r.user)))
  }

  def viewAll = SecuredAction { implicit r =>
    Ok(views.html.ratings())
  }

  private def get(titleId: Int, ratingType: RatingType)
                 (implicit r: SecuredRequest[_]) = {
    val rating = generalPersistenceService.ratingFor(r.user.id, titleId, ratingType)
    Ok(rating.map(_.rating.toString).getOrElse("N/A"))
  }

  private def post(titleId: Int, ratingType: RatingType)
                  (implicit r: SecuredRequest[_]) = {
    val boundForm = ratingForm.bindFromRequest()
    val title = titleRepository(titleId)
    val rating = boundForm.get.rating.toDouble
    generalPersistenceService.setRating(r.user.id, titleId, ratingType, rating)
    notificationsActor ! SocketMessage(s"rated ${Link(title)}", r.user)
    Ok("Rating set")
  }

  private def cancel(titleId: Int, ratingType: RatingType)
                    (implicit r: SecuredRequest[_]) = {
    generalPersistenceService.cancelRating(r.user.id, titleId, ratingType)
    Ok("Rating canceled")
  }
}
