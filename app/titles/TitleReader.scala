package titles

import java.io.File

import com.github.tototoshi.csv.CSVReader
import model.Title

import scala.util.control.NonFatal

object TitleKeys {
  val Title = "Title"
  val Rank = "Rank"
  val Year = "Year"
}

class TitleReader {
  def read(location: String): Seq[Title] = {
    val reader = CSVReader.open(new File("conf/" + location))
    reader.allWithHeaders().zipWithIndex.map { case (stringMap, i) =>
      try {
        val title = stringMap(TitleKeys.Title).replace("\u00a0","").trim
        Title(i, title, stringMap(TitleKeys.Year).toInt, stringMap(TitleKeys.Rank).toInt)
      } catch {
        case NonFatal(t) =>
          println(s"Error while processing line: $stringMap")
          throw t
      }
    }
  }
}