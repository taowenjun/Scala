trait RemoteMessage extends Serializable

case class RegisterWorker(id:String,cores:Int,memory:Int) extends RemoteMessage

case class RegisteredWorker(url:String) extends RemoteMessage

case class HeartBeat(id:String) extends RemoteMessage

case object SendHeartBeat

case object CheckTimeoutWorker