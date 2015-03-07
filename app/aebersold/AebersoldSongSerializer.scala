package aebersold

import serialization.{JsonSerialization, JsonSerializer}
import org.json4s.jackson.Serialization._

class AebersoldSongSerializer extends JsonSerializer[AebersoldSong] {
  val key = "songs"
  import JsonSerialization._
  override def serialize(single: AebersoldSong): String = write(single)
  override def serializeMany(sequence: Seq[AebersoldSong]): String = s"""{"$key":${write(sequence)}}"""
}
