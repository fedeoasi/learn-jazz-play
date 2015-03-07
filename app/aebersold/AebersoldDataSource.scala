package aebersold

import java.util.Random

class AebersoldDataSource(location: String) {
  val reader = new AebersoldReader
  val r = new Random
  val dataset = reader.read(location)

  def all: Seq[AebersoldSong] = dataset

  def random: AebersoldSong = {
    val index = r.nextInt(dataset.size)
    dataset(index)
  }
}
