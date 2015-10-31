package serialization

import model.Video
import org.json4s.jackson.Serialization._

class VideosSerializer extends JsonSerializer[Video] {
  import JsonSerialization._
  val key = "videos"

  override def serialize(single: Video): String = write(single)

  override def serializeMany(sequence: Seq[Video]): String = s"""{"$key":${write(sequence)}}"""
}
