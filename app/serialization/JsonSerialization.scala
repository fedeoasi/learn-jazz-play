package serialization

import model.Activity
import org.joda.time.DateTime
import org.json4s.JsonAST.JString
import org.json4s.{FieldSerializer, CustomSerializer, DefaultFormats}
import persistence.general.RatingType

object JsonSerialization {
  implicit lazy val formats = DefaultFormats +
    new DateTimeSerializer +
    new RatingTypeSerializer +
    new FieldSerializer[Activity]()

  class DateTimeSerializer extends CustomSerializer[DateTime](format => (
    { case JString(t) => DateTime.parse(t) },
    { case t: DateTime => JString(t.toString) }
    )
  )

  class RatingTypeSerializer extends CustomSerializer[RatingType](format => (
    { case JString(t) => RatingType(t) },
    { case t: RatingType => JString(t.discriminator) }
    )
  )
}
