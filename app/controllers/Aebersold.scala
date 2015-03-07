package controllers

import aebersold.{AebersoldSongSerializer, AebersoldDataSource}
import com.google.inject.Inject
import play.api.mvc.Action
import play.api.mvc.Results._

class Aebersold @Inject() (aebersoldDataSource: AebersoldDataSource) {
  val serializer = new AebersoldSongSerializer

  def all = Action { request =>
    Ok(serializer.serializeMany(aebersoldDataSource.all))
  }

  def random = Action { request =>
    Ok(serializer.serialize(aebersoldDataSource.random))
  }
}
