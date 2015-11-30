package titles

import model.Title

object TitleSpecHelper {
  val title = Title(10, "Ten", 2010, 3)
  val title2 = Title(12, "Twelve", 1986, 4)
  val titleRepository = buildTitleRepository()

  private def buildTitleRepository(): TitleRepository = {
    new InMemoryTitleRepository(Seq(title, title2))
  }
}
