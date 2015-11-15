package titles

import java.io.{Reader, File}

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
        Title(i, title, stringMap(TitleKeys.Year).toInt, stringMap(TitleKeys.Rank).toInt)
      } catch {
        case NonFatal(t) =>
          println(s"Error while processing line: $stringMap")
          throw t
      }
    }
  }
}