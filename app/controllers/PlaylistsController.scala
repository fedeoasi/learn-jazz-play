package controllers

import com.google.inject.Inject
import playlist.{HybridPlaylist, PlaylistGenerator, PlaylistSerializer}
import securesocial.core.{RuntimeEnvironment, SecureSocial}
import service.User
import titles.TitleDataSource

class PlaylistsController @Inject() (titleDataSource: TitleDataSource,
                                     generator: PlaylistGenerator)
                                    (override implicit val env: RuntimeEnvironment[User])
  extends SecureSocial[User] {

  val serializer = new PlaylistSerializer

  def generate() = SecuredAction { implicit r =>
    val playlist = generator.generatePlaylist(r.user, HybridPlaylist)
    Ok(serializer.serialize(playlist))
  }

  def view() = SecuredAction { implicit r =>
    Ok(views.html.playlist())
  }
}
