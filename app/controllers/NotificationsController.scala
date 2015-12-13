package controllers

import akka.actor.ActorRef
import com.google.inject.Inject
import com.google.inject.name.Named
import play.api.Play.current
import play.api.mvc.WebSocket
import realtime.MyWebSocketActor
import realtime.NotificationsActor.{SocketConnect, SocketMessage}
import securesocial.MyRuntimeEnvironment
import securesocial.core.SecureSocial

class NotificationsController @Inject() (@Named("notifications") notificationsActor: ActorRef,
                                         override implicit val env: MyRuntimeEnvironment)
  extends SecureSocial {

  def socket() = WebSocket.acceptWithActor[String, String] { request =>
    out =>
      notificationsActor ! SocketConnect(out)
      MyWebSocketActor.props(out, notificationsActor)
  }

  def hello() = SecuredAction { implicit r =>
    notificationsActor ! SocketMessage("Hello")
    Ok("Done")
  }
}
