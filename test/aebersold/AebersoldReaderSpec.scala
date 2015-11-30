package aebersold

import org.scalatest.{FunSpec, Matchers}

class AebersoldReaderSpec extends FunSpec with Matchers {
  describe("Aeberson Reader") {
    it("parses the aebersold file") {
      val reader = new AebersoldReader
      val aebersold = reader.read("aebersold_index.csv")
      aebersold.size shouldBe 1560
      val first = aebersold.head
      first.title shouldBe "Blues In Bb**"
      val last = aebersold.last
      last.title shouldBe "Ballin' The Jack"
    }
  }
}
