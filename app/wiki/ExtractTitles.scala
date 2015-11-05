package wiki

import org.jsoup.Jsoup

import scala.collection.JavaConverters._

object ExtractTitles {
  val JazzStandardsUrl = "https://en.wikipedia.org/wiki/List_of_jazz_standards"

  case class Title(name: String, link: String)

  def main(args: Array[String]) {
    val titles = downloadTitles()
    titles.foreach(println)
    println(s"Found ${titles.size} titles")
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
