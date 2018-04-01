package standards

import org.scalatest.{FunSpec, Matchers}
import titles.TitleReader

class TitleReaderSpec extends FunSpec with Matchers {
  describe("Title Reader") {
    it("parses the jazz standards file") {
      val reader = new TitleReader
      val titles = reader.read("jazz_standards.csv")
      titles.size should be(1000)
      val first = titles.head
      first.title should be("Body and Soul")
      val last = titles.last
      last.title should be("To Each His Own")
    }
  }
}
