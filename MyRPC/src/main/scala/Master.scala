import akka.actor.{Actor,ActorSystem,Props}
import com.typesafe.config.ConfigFactory

import scala.collection.mutable
import scala.concurrent.duration._

class Master(host:String,port:Int) extends Actor{
  var idToWorker = new mutable.HashMap[String,WorkerInfo]()
  var workers = new mutable.HashSet[WorkerInfo]()
  val checkInterval = 3000

  override def preStart(): Unit = {
    println("master is started...")
    import context.dispatcher
    context.system.scheduler.schedule(0 millis,checkInterval millis,self,CheckTimeoutWorker)
  }

  override def receive: Receive = {
    case CheckTimeoutWorker=>{
      val time = System.currentTimeMillis()
      val toRemove = workers.filter(x =>time - x.lastHeartBeat>checkInterval)
      for(worker<-toRemove){
        idToWorker -= worker.id
        workers -= worker
      }
      println(workers.size)
    }

    case RegisterWorker(id,memory,cores)=>{
      if(!idToWorker.contains(id)){
        val info=new WorkerInfo(id,memory,cores)
        idToWorker(id)=info
        workers += info
        sender!RegisteredWorker(s"akka.tcp://MasterSystem@$host:$port/user/Master")
        println(s"a worker: $id has connected")
      }
    }

    case HeartBeat(id)=>{
      if(idToWorker.contains(id)){
        val workerInfo=idToWorker(id)
        val currentTime=System.currentTimeMillis()
        workerInfo.lastHeartBeat = currentTime
      }
    }
  }
}

object Master{
  def main(args: Array[String]): Unit = {
    val host="10.108.20.121"
    val port=6667

    val configStr=
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem("MasterSystem",config)
    val master = actorSystem.actorOf(Props(new Master(host,port)),"Master")
    actorSystem.awaitTermination()
  }
}

