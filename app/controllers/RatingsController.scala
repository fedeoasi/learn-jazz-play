package controllers

import com.google.inject.Inject
import model.RatingType
import model.RatingTypes._
import persistence.general.GeneralPersistenceService
import play.api.data.Form
import play.api.data.Forms._
import securesocial.MyRuntimeEnvironment
import securesocial.core.{RuntimeEnvironment, SecureSocial}
import serialization.TitleWithRatingSerializer
import service.{RatingService, User}

class RatingsController @Inject() (generalPersistenceService: GeneralPersistenceService,
                                   ratingService: RatingService,
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
    val rating = boundForm.get.rating.toDouble
    generalPersistenceService.setRating(r.user.id, titleId, ratingType, rating)
    Ok("Rating set")
  }

  private def cancel(titleId: Int, ratingType: RatingType)
                    (implicit r: SecuredRequest[_]) = {
    generalPersistenceService.cancelRating(r.user.id, titleId, ratingType)
    Ok("Rating canceled")
  }
}
