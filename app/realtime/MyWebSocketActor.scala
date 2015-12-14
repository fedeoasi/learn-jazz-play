package realtime

import akka.actor._
import realtime.NotificationsProtocol.SocketDisconnect
import service.User

object MyWebSocketActor {
  def props(out: ActorRef, notificationsActor: ActorRef, user: User) = {
    Props(new MyWebSocketActor(out, notificationsActor, user))
  }
}

class MyWebSocketActor(out: ActorRef, notificationsActor: ActorRef, user: User) extends Actor {
  def receive = {
    case msg: String =>
      out ! s"I received your message from actor $this: $msg"
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    notificationsActor ! SocketDisconnect(user, out)
    super.postStop()
  }
}
