package wiki

import java.io.{File, FileReader}

import com.github.tototoshi.csv.CSVWriter
import org.jsoup.Jsoup
import titles.InMemoryTitleRepository

import scala.collection.JavaConverters._

object DownloadTitlesFromWikipedia {
  val JazzStandardsUrl = "https://en.wikipedia.org/wiki/List_of_jazz_standards"
  val OutputWikiFilePath = "conf/titles_wiki.csv"

  case class Title(name: String, link: String)

  def main(args: Array[String]) {
    val titles = downloadTitles()
    val titleRepository = InMemoryTitleRepository(new FileReader("conf/jazz_standards.csv"))

    val titlesByName = titleRepository.all.groupBy(_.title)

    println(s"Found ${titles.size} titles")
    val count = titles.count(t => titlesByName.contains(t.name))
    println(s"Our repository includes $count of the standards on Wikipedia")

    writeCsv(titles, OutputWikiFilePath)
  }

  def writeCsv(titles: Seq[Title], filePath: String): Unit = {
    val csvWriter = CSVWriter.open(new File(filePath))
    csvWriter.writeRow(Seq("Title", "Link"))
    csvWriter.writeAll(titles.map { t => Seq[String](t.name, t.link) })
    csvWriter.close()
  }

  def downloadTitles(): Seq[Title] = {
    val doc = Jsoup.connect(JazzStandardsUrl).get()
    val links = doc.select("#bodyContent div.div-col ul li a")
    val titles = links.asScala.map { l =>
      Title(l.text(), l.attr("href"))
    }
    titles.toSeq
  }
}
