import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._

class Worker(masterHost:String,masterPort:Int,cores:Int,memory:Int) extends Actor {

  val id:String = UUID.randomUUID().toString
  var master:ActorSelection = _
  val heartBeatInterval : Int = 2000

  override def preStart():Unit={
    master=context.actorSelection(s"akka.tcp://MasterSystem@$masterHost:$masterPort/user/Master")
    master!RegisterWorker(id,cores,memory)
    println("register...")
  }

  override def receive: Receive = {
    case RegisteredWorker(masterURL)=>{
      println(s"The worker has connected to master:$masterURL")
      import context.dispatcher
      context.system.scheduler.schedule(0 millis,heartBeatInterval millis,self,SendHeartBeat)
    }

    case SendHeartBeat=>{
      master!HeartBeat(id)
      println("send heart beat")
    }
  }
}

object Worker{
  def main(args: Array[String]): Unit = {
    val masterHost="10.108.20.121"
    val masterPort:Int=6667

    val host="10.108.20.121"
    val port=7777

    val memory=200
    val cores=4

    val configStr=
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config=ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem("WorkerSystem",config)
    actorSystem.actorOf(Props(new Worker(masterHost,masterPort,cores,memory)),"Worker")
    actorSystem.awaitTermination()
  }
}
