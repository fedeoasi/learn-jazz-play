package playlist

import titles.TitleDataSource

trait PlaylistGenerator {
  def generatePlaylist: Playlist
}

class PlaylistGeneratorImpl(titleSource: TitleDataSource) {
  def generatePlaylist: Playlist = {
    val titles = 1 to 10 map { _ => titleSource.random }
    Playlist(titles)
  }
}
