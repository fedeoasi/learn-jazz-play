package controllers

import aebersold.{AebersoldSongSerializer, AebersoldDataSource}
import com.google.inject.Inject
import securesocial.core.RuntimeEnvironment
import service.User

class Aebersold @Inject() (dataSource: AebersoldDataSource, override implicit val env: RuntimeEnvironment[User]) extends securesocial.core.SecureSocial[User] {
  val serializer = new AebersoldSongSerializer

  def all = SecuredAction { implicit request =>
    Ok(serializer.serializeMany(dataSource.all))
  }

  def random = SecuredAction { implicit request =>
    Ok(serializer.serialize(dataSource.random))
  }
}
