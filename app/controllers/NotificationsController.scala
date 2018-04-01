package controllers

import akka.actor.ActorRef
import com.google.inject.Inject
import com.google.inject.name.Named
import play.api.Play.current
import play.api.mvc.WebSocket
import play.api.mvc.WebSocket.HandlerProps
import realtime.MyWebSocketActor
import realtime.NotificationsProtocol.{SocketConnect, SocketMessage}
import securesocial.CustomRuntimeEnvironment
import securesocial.core.SecureSocial

class NotificationsController @Inject() (@Named("notifications") notificationsActor: ActorRef,
                                         override implicit val env: CustomRuntimeEnvironment)
  extends SecureSocial {

  def socket() = {
    WebSocket.tryAcceptWithActor[String, String] { implicit request =>
      SecureSocial.currentUser.map {
        case Some(user) =>
          Right(handlerProps(user))
        case None =>
          Left(Forbidden)
      }
    }
  }

  private def handlerProps(user: env.U): HandlerProps = { out =>
      notificationsActor ! SocketConnect(user, out)
      MyWebSocketActor.props(out, notificationsActor, user)
  }

  def hello() = SecuredAction { implicit r =>
    notificationsActor ! SocketMessage(r.user, "Hello")
    Ok("Done")
  }
}
