package serialization

import org.joda.time.DateTime
import org.json4s.JsonAST.JString
import org.json4s.{FieldSerializer, CustomSerializer, DefaultFormats}
import service.Activity

object JsonSerialization {
  implicit lazy val formats = DefaultFormats + new DateTimeSerializer + new FieldSerializer[Activity]()

  class DateTimeSerializer extends CustomSerializer[DateTime](format => (
    { case JString(t) => DateTime.parse(t) },
    { case t: DateTime => JString(t.toString) }
    ))
}
