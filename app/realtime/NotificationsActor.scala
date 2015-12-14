package realtime

import akka.actor.{ActorRef, Actor}
import service.User

class NotificationsActor extends Actor {
  import NotificationsActor._

  var socketsByUser: Map[User, List[SocketConnect]] = Map.empty

  override def receive: Receive = {
    case s: SocketConnect =>
      val socketsForUser = socketsByUser.getOrElse(s.user, List.empty[SocketConnect])
      socketsByUser = socketsByUser.updated(s.user, s :: socketsForUser)
    case SocketDisconnect(user, out) =>
      val socketsForUser = socketsByUser.getOrElse(user, List.empty[SocketConnect])
      val withoutSocket = socketsForUser.filter(_.out != out)
      socketsByUser = socketsByUser.updated(user, withoutSocket)
    case SocketMessage(user, msg) =>
      socketsByUser.foreach { case (u, ss) =>
        val subject = if (u == user) "You" else "Somebody"
        ss.foreach(_.out ! s"$subject $msg")
      }
  }
}

object NotificationsActor {
  case class SocketConnect(user: User, out: ActorRef)
  case class SocketDisconnect(user: User, out: ActorRef)
  case class SocketMessage(user: User, msg: String)
}
