package serialization

import org.json4s.jackson.Serialization._
import service.TitleWithRating

class TitleWithRatingSerializer extends JsonSerializer[TitleWithRating] {
  import JsonSerialization._
  val key = "titlesWithRatings"

  override def serialize(single: TitleWithRating): String = write(single)

  override def serializeMany(sequence: Seq[TitleWithRating]): String = {
    s"""{"$key":${write(sequence)}}"""
  }

}
