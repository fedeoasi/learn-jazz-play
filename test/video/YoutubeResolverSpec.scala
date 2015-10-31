package video

import org.scalatest.{FunSpec, Matchers}
import play.api.test.FakeApplication

class YoutubeResolverSpec extends FunSpec with Matchers {
  private implicit val application = FakeApplication()
  private val resolver = new YoutubeResolver
  private val urls = Seq(
    "http://www.youtube.com/watch?v=j4dMnAPZu70",
    "http://www.youtube.com/v/j4dMnAPZu70?fs=1&hl=en_US",
    "http://youtu.be/j4dMnAPZu70",
    "https://youtu.be/j4dMnAPZu70",
    "http://www.youtube.com/embed/j4dMnAPZu70",
    "https://www.youtube.com/embed/j4dMnAPZu70",
    "https://www.youtube-nocookie.com/embed/j4dMnAPZu70",
    "http://y2u.be/j4dMnAPZu70",
    "http://www.youtube.com/watch?v=j4dMnAPZu70&feature=youtu.be"
  )

  describe("Youtube Resolver") {
    describe("Id extraction") {
      it("should extract the id from a regular url") {
        for (u <- urls) {
          withClue(s"Extracting id from: $u") {
            resolver.extractVideoId(u) should be(Some("j4dMnAPZu70"))
          }
        }
      }
    }
  }
}
