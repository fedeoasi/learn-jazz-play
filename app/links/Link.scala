package links

import model.Title

object Link {
  def apply(title: Title): String = s"""<a href="titles/${title.id}">${title.title}</a>"""
}
