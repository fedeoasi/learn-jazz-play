package titles

import java.io.{Reader, File}

import com.github.tototoshi.csv.CSVReader
import model.Title

import scala.util.control.NonFatal

object TitleKeys {
  val Title = "Title"
  val Rank = "Rank"
  val Year = "Year"
  val Link = "Link"
}

class TitleReader {
  def read(location: String): Seq[Title] = {
    val file = new File("conf/" + location)
    val csvReader = CSVReader.open(file)
    read(csvReader)
  }

  def read(reader: Reader): Seq[Title] = {
    val csvReader = CSVReader.open(reader)
    read(csvReader)
  }

  private def read(csvReader: CSVReader): List[Title] = {
    csvReader.allWithHeaders().zipWithIndex.map { case (stringMap, i) =>
      try {
        val title = stringMap(TitleKeys.Title).replace("\u00a0", "").trim
        val link = {
          val linkValue = stringMap(TitleKeys.Link)
          if (linkValue.isEmpty) None else Some(linkValue)
        }
        Title(i, title, stringMap(TitleKeys.Year).toInt, stringMap(TitleKeys.Rank).toInt, link)
      } catch {
        case NonFatal(t) =>
          println(s"Error while processing line: $stringMap")
          throw t
      }
    }
  }
}