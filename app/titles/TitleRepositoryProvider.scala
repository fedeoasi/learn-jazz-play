package titles

import java.io.InputStreamReader

import com.google.inject.Provider
import play.api.Play

class TitleRepositoryProvider extends Provider[TitleRepository] {
  private lazy val titleRepository = {
    val resource = Play.current.classloader.getResourceAsStream("jazz_standards_with_wiki.csv")
    val reader = new InputStreamReader(resource)
    InMemoryTitleRepository(reader)
  }

  override def get(): TitleRepository = titleRepository
}