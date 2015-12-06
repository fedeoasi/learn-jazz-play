package model

case class Title(id: Int,
                 title: String,
                 year: Int,
                 ranking: Int,
                 link: Option[String])