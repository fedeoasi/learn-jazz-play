package controllers

import com.google.inject.Inject
import securesocial.core.RuntimeEnvironment
import service.User
import titles.{TitleDataSource, TitleSerializer}

class TitlesController @Inject() (dataSource: TitleDataSource,
                        override implicit val env: RuntimeEnvironment[User])
  extends securesocial.core.SecureSocial[User] {

  val serializer = new TitleSerializer

  def index(id: Int) = SecuredAction { implicit request =>
    val title = dataSource.get(id).fold("Title")(_.title)
    Ok(views.html.title(id, title))
  }

  def all = SecuredAction { implicit request =>
    Ok(serializer.serializeMany(dataSource.all))
  }

  def random = SecuredAction { implicit request =>
    Ok(serializer.serialize(dataSource.random))
  }

  def get(id: Int) = SecuredAction { implicit request =>
    dataSource.get(id) match {
      case Some(t) => Ok(serializer.serialize(t))
      case None => NotFound(s"Did not find title with id $id")
    }
  }
}