package serialization

import org.json4s.jackson.Serialization._
import service.SerializedActivity

class BaseActivitySerializer extends JsonSerializer[SerializedActivity] {
  import JsonSerialization._
  val key = "activities"

  override def serialize(single: SerializedActivity): String = write(single)
  override def serializeMany(sequence: Seq[SerializedActivity]): String = s"""{"$key":${write(sequence)}}"""
}
