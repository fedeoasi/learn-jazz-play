package serialization

import org.json4s.jackson.Serialization._
import service.BaseActivity

class BaseActivitySerializer extends JsonSerializer[BaseActivity] {
  import JsonSerialization._
  val key = "activities"

  override def serialize(single: BaseActivity): String = write(single)
  override def serializeMany(sequence: Seq[BaseActivity]): String = s"""{"$key":${write(sequence)}}"""
}
