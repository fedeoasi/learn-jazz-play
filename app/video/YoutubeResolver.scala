package video

import java.net.URL

import play.api.{Application, Logger}
import play.api.libs.ws.WS

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class YoutubeResolver(implicit application: Application) {
  private val logger = Logger.logger
  private val emptyMap = Map.empty[String, String]
  private val hostWhiteList = Set[String]("youtube.com", "youtu.be", "y2u.be", "youtube-nocookie.com")
  private val expectedIdLength = 11

  def resolveUrl(urlString: String): String = {
    val futureResponse = WS.url(urlString).get()
    Await.result(futureResponse, 2.seconds).body
  }

  def extractVideoId(urlString: String): Option[String] = {
    Try(new URL(urlString)) match {
      case Success(url) =>
        if (matchHost(url)) {
          queryParamsMap(url).get("v").orElse {
            extractLastPathPart(url)
          }
        } else None
      case Failure(t) =>
        logger.error(urlString)
        None
    }
  }

  def extractLastPathPart(url: URL): Option[String] = {
    val pathParts = url.getPath.split("/")
    if (pathParts.nonEmpty && pathParts.last.length == expectedIdLength) {
      Some(pathParts.last)
    } else None
  }

  def matchHost(url: URL): Boolean = {
    hostWhiteList.contains(url.getHost.replace("www.", ""))
  }

  private def queryParamsMap(url: URL): Map[String, String] = {
    Option(url.getQuery).fold(emptyMap) { query =>
      query.split("&").map { expr =>
        val keyAndValue = expr.split("=")
        (keyAndValue.head, keyAndValue(1))
      }.toMap
    }
  }
}
