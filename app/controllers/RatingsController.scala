package controllers

import com.google.inject.Inject
import persistence.general.{LikeRating, RatingType, KnowRating, GeneralPersistenceService}
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Request
import securesocial.core.{RuntimeEnvironment, SecureSocial}
import service.User

class RatingsController @Inject() (generalPersistenceService: GeneralPersistenceService,
                                   override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  case class RatingFormData(rating: String)

  private val ratingForm = Form(mapping("rating" -> text)(RatingFormData.apply)(RatingFormData.unapply))

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

  private def get(titleId: Int, ratingType: RatingType)(implicit r: Request[_]) = {
    val rating = generalPersistenceService.ratingFor(1, titleId, ratingType)
    Ok(rating.map(_.toString).getOrElse("N/A"))
  }

  private def post(titleId: Int, ratingType: RatingType)(implicit r: Request[_]) = {
    val boundForm = ratingForm.bindFromRequest()
    val rating = boundForm.get.rating.toDouble
    generalPersistenceService.setRating(1, titleId, ratingType, rating)
    Ok("Rating set")
  }

  private def cancel(titleId: Int, ratingType: RatingType)(implicit r: Request[_]) = {
    generalPersistenceService.cancelRating(1, titleId, ratingType)
    Ok("Rating canceled")
  }
}
