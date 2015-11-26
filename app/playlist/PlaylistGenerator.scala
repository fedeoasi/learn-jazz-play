package playlist

import java.util.Random

import com.google.inject.{ImplementedBy, Inject}
import persistence.general.GeneralPersistenceService
import service.User
import titles.TitleDataSource

sealed trait PlaylistType
case object RandomPlaylist extends PlaylistType
case object KnownPlaylist extends PlaylistType
case object HybridPlaylist extends PlaylistType

@ImplementedBy(classOf[PlaylistGeneratorImpl])
trait PlaylistGenerator {
  def generatePlaylist(user: User, playlistType: PlaylistType = RandomPlaylist): Playlist
}

class PlaylistGeneratorImpl @Inject() (titleSource: TitleDataSource,
                                       gps: GeneralPersistenceService) extends PlaylistGenerator {
  private val PlaylistSize = 10
  private val random = new Random()

  def generatePlaylist(user: User, playlistType: PlaylistType): Playlist = {
    playlistType match {
      case RandomPlaylist => generateRandomPlaylist()
      case KnownPlaylist => generateKnownPlaylist(user)
      case HybridPlaylist => generateHybridPlaylist(user)
    }
  }

  def generateRandomPlaylist(): Playlist = {
    val titles = 1 to PlaylistSize map { _ => titleSource.random }
    Playlist(titles)
  }

  def generateKnownPlaylist(user: User): Playlist = {
    val knownTitles = gps.knownTitlesFor(user.id)
    val resultSize = PlaylistSize.min(knownTitles.size)
    val chosenTitleIds = randomlySelectFrom(knownTitles.toSeq, resultSize)
    Playlist(chosenTitleIds.toSeq.map(titleSource.apply))
  }

  def generateHybridPlaylist(user: User): Playlist = {
    val knownTitles = gps.knownTitlesFor(user.id)
    val randomTitles = generateRandomPlaylist().titles.map(_.id)
    val ids = knownTitles ++ randomTitles
    val selectedIds = randomlySelectFrom(ids.toSeq, PlaylistSize)
    Playlist(selectedIds.map(titleSource.apply))
  }

  private def randomlySelectFrom(source: Seq[Int], n: Int): Seq[Int] = {
    val array = source.toArray
    (1 to n).foldLeft(List.empty[Int]) { case (titles, i) =>
      val selectionSize = array.length - i + 1
      val selected = if (selectionSize > 0) {
        val selectedIndex = random.nextInt(selectionSize)
        val r = array(selectedIndex)
        swap(array, selectedIndex, selectionSize - 1)
        r
      } else {
        array(0)
      }
      selected :: titles
    }
  }

  private def swap(array: Array[Int], from: Int, to: Int): Unit = {
    val tmp = array(to)
    array(to) = array(from)
    array(from) = tmp
  }
}
