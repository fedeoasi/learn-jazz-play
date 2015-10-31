package controllers

import aebersold.{AebersoldSongSerializer, AebersoldDataSource}
import com.google.inject.Inject
import securesocial.core.RuntimeEnvironment
import service.User
import securesocial.core.SecureSocial

class AebersoldController @Inject() (dataSource: AebersoldDataSource,
                                     override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  val serializer = new AebersoldSongSerializer

  def index = SecuredAction { implicit request =>
    Ok(views.html.aebersold())
  }

  def all = SecuredAction { implicit request =>
    Ok(serializer.serializeMany(dataSource.all))
  }

  def random = SecuredAction { implicit request =>
    Ok(serializer.serialize(dataSource.random))
  }
}
