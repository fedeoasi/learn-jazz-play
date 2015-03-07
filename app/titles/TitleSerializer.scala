package titles

import model.Title
import org.json4s.jackson.Serialization._
import serialization.{JsonSerialization, JsonSerializer}

class TitleSerializer extends JsonSerializer[Title] {
  import JsonSerialization._
  val key = "titles"
  override def serialize(single: Title): String = write(single)
  override def serializeMany(sequence: Seq[Title]): String = s"""{"$key":${write(sequence)}}"""
}