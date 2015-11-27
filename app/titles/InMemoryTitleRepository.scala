package titles

import java.io.{FileReader, Reader}
import java.util.Random

import model.Title

class InMemoryTitleRepository(dataset: Seq[Title]) extends TitleRepository {
  val r = new Random
  val byId = dataset.groupBy(_.id).mapValues(_.head)

  override def all: Seq[Title] = dataset

  override def random: Title = dataset(randomId)

  override def randomId: Int = r.nextInt(dataset.size)

  override def get(id: Int): Option[Title] = byId.get(id)

  override def apply(id: Int): Title = byId(id)
}

object InMemoryTitleRepository {
  def apply(fileReader: Reader): InMemoryTitleRepository = {
    val reader = new TitleReader
    val dataset = reader.read(fileReader)
    new InMemoryTitleRepository(dataset)
  }
}