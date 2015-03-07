package aebersold

import java.io.File

import com.github.tototoshi.csv.CSVReader

import scala.util.Try
import scala.util.control.NonFatal

object AebersoldKeys {
  val Title = "Tune Title"
  val Type = "Vehicle Type"
  val Key = "Key"
  val Tempo = "Tempo"
  val ChorusNum = "# of choruses"
  val VolumeNum = "Vol #"
  val TrackNum = "CD Track #"
}

class AebersoldReader {
  type AebersoldSongs = Seq[AebersoldSong]

  import aebersold.AebersoldKeys._

  def read(location: String): AebersoldSongs = {
    val reader = CSVReader.open(new File("conf/" + location))
    reader.allWithHeaders().zipWithIndex.map { case (stringMap, i) =>
      try {
        val tempo = Try(stringMap(Tempo).toInt).toOption
        AebersoldSong(i, stringMap(Title), stringMap(Type), stringMap(Key), tempo, stringMap(ChorusNum), stringMap(VolumeNum), stringMap(TrackNum))
      } catch {
        case NonFatal(t) =>
          println(s"Error while processing line: $stringMap")
          throw t
      }
    }
  }
}

case class AebersoldSong(id: Int, title: String, songType: String, key: String, tempo: Option[Int], chorusNum: String, volumeNum: String, trackNum: String)