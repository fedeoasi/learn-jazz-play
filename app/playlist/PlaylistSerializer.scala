package playlist

import org.json4s.jackson.Serialization._
import serialization.{JsonSerialization, JsonSerializer}

class PlaylistSerializer extends JsonSerializer[Playlist] {
  import JsonSerialization._
  val key = "playlists"

  override def serialize(single: Playlist): String = write(single)

  override def serializeMany(sequence: Seq[Playlist]): String = s"""{"$key":${write(sequence)}}"""

}
