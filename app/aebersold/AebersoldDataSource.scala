package aebersold

trait AebersoldDataSource {
  def all: Seq[AebersoldSong]
  def random: AebersoldSong
}
