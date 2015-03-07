package serialization

trait JsonSerializer[T] {
  def serialize(single: T): String
  def serializeMany(sequence: Seq[T]): String
}
