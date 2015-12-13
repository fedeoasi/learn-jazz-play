package realtime

import akka.actor._
import realtime.NotificationsActor.SocketDisconnect

object MyWebSocketActor {
  def props(out: ActorRef, notificationsActor: ActorRef) = {
    Props(new MyWebSocketActor(out, notificationsActor))
  }
}

class MyWebSocketActor(out: ActorRef, notificationsActor: ActorRef) extends Actor {
  def receive = {
    case msg: String =>
      out ! s"I received your message from actor $this: $msg"
  }

  @throws[Exception](classOf[Exception])
  override def postStop(): Unit = {
    notificationsActor ! SocketDisconnect(out)
    super.postStop()
  }
}
