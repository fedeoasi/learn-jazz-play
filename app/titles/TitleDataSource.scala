package titles

import model.Title

trait TitleDataSource {
  def all: Seq[Title]
  def random: Title
  def randomId: Int
  def get(id: Int): Option[Title]
  def apply(id: Int): Title
}
