package serialization

import org.json4s.DefaultFormats

object JsonSerialization {
  implicit lazy val formats = DefaultFormats
}
