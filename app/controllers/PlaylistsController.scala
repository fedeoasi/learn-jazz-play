package controllers

import com.google.inject.Inject
import playlist.{PlaylistGeneratorImpl, PlaylistSerializer}
import securesocial.core.{SecureSocial, RuntimeEnvironment}
import service.User
import titles.TitleDataSource


class PlaylistsController @Inject() (titleDataSource: TitleDataSource)
                                    (override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  val serializer = new PlaylistSerializer
  val generator = new PlaylistGeneratorImpl(titleDataSource)

  def generate() = SecuredAction { implicit r =>
    val playlist = generator.generatePlaylist
    Ok(serializer.serialize(playlist))
  }

}
