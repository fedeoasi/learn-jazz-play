package serialization

import org.json4s.jackson.Serialization._
import service.UserStats

class UserStatsSerializer extends JsonSerializer[UserStats] {
  import JsonSerialization._
  val key = "userStats"

  override def serialize(single: UserStats): String = write(single)

  override def serializeMany(sequence: Seq[UserStats]): String = {
    s"""{"$key":${write(sequence)}}"""
  }
}
