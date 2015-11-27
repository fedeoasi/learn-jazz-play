package controllers

import com.google.inject.Inject
import securesocial.core.RuntimeEnvironment
import service.User
import titles.{TitleRepository, TitleSerializer}

class TitlesController @Inject() (titleRepository: TitleRepository,
                                  override implicit val env: RuntimeEnvironment[User])
  extends securesocial.core.SecureSocial[User] {

  val serializer = new TitleSerializer

  def index(id: Int) = SecuredAction { implicit request =>
    val title = titleRepository.get(id).fold("Title")(_.title)
    Ok(views.html.title(id, title))
  }

  def all = SecuredAction { implicit request =>
    Ok(serializer.serializeMany(titleRepository.all))
  }

  def random = SecuredAction { implicit request =>
    Ok(serializer.serialize(titleRepository.random))
  }

  def get(id: Int) = SecuredAction { implicit request =>
    titleRepository.get(id) match {
      case Some(t) => Ok(serializer.serialize(t))
      case None => NotFound(s"Did not find title with id $id")
    }
  }

  def viewAll = SecuredAction { implicit r =>
    Ok(views.html.titles())
  }
}