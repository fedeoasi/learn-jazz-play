package titles

import java.io.Reader
import java.util.Random

import model.Title

class InMemoryTitleDataSource(fileReader: Reader) extends TitleDataSource {
  val reader = new TitleReader
  val r = new Random
  val dataset = reader.read(fileReader)
  val byId = dataset.groupBy(_.id).mapValues(_.head)

  def all: Seq[Title] = dataset

  def random: Title = dataset(randomId)

  def randomId: Int = r.nextInt(dataset.size)

  def get(id: Int): Option[Title] = byId.get(id)
}
