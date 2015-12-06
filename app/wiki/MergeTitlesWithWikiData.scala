package wiki

import java.io.File

import com.github.tototoshi.csv.{CSVWriter, CSVReader}

object MergeTitlesWithWikiData {
  val JazzStandardsPath = "conf/jazz_standards.csv"
  val TitlesWikiPath = "conf/titles_wiki.csv"

  def main(args: Array[String]) {
    val titleReader = CSVReader.open(JazzStandardsPath)
    val wikiReader = CSVReader.open(TitlesWikiPath)
    val allTitles = titleReader.allWithHeaders()
    val allWikiTitles = wikiReader.allWithHeaders()

    val wikiTitlesByName = allWikiTitles.groupBy(_("Title"))

    val withWiki = allTitles.map { t =>
      wikiTitlesByName.get(t("Title")) match {
        case Some(wikiMap) => t ++ wikiMap.head
        case None => t
      }
    }
    writeCsv(withWiki, "conf/with_wiki.csv")
  }

  def writeCsv(titles: Seq[Map[String, String]], filePath: String): Unit = {
    val keys = titles.head.keySet.toSeq
    val csvWriter = CSVWriter.open(new File(filePath))
    csvWriter.writeRow(keys)
    val seqs = titles.map { t =>
      keys.map(t.getOrElse(_, ""))
    }
    csvWriter.writeAll(seqs)
    csvWriter.close()
  }
}
