package realtime

import akka.actor.{ActorRef, Actor}


class NotificationsActor extends Actor {
  import NotificationsActor._

  var sockets: List[SocketConnect] = List.empty

  override def receive: Receive = {
    case s: SocketConnect =>
      sockets = s :: sockets
      println(s"${sockets.size} clients")
    case SocketDisconnect(out) =>
      sockets = sockets.filter(_.out != out)
    case SocketMessage(msg) =>
      sockets.foreach(_.out ! msg)
  }
}

object NotificationsActor {
  case class SocketConnect(out: ActorRef)
  case class SocketDisconnect(out: ActorRef)
  case class SocketMessage(msg: String)
}
