package model

import org.joda.time.DateTime

case class VideoInput(videoId: String)
case class Video(videoId: String, modifiedTime: DateTime, id: Option[Int] = None)
