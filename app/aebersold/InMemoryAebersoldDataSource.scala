package aebersold

import java.util.Random

class InMemoryAebersoldDataSource(location: String) extends AebersoldDataSource {
  val reader = new AebersoldReader
  val r = new Random
  val dataset = reader.read(location)

  def all: Seq[AebersoldSong] = dataset

  def random: AebersoldSong = {
    val index = r.nextInt(dataset.size)
    dataset(index)
  }
}
