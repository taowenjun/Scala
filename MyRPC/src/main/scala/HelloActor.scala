import akka.actor.{Actor, ActorSystem, Props}

class HelloActor extends Actor{
  def receive:Receive={
    case "hello"=>println("hello world")
    case _ => println("enheng")
  }
}

object Main extends App{
  val system = ActorSystem("HelloSystem")
  val helloActor = system.actorOf(Props[HelloActor],"helloactor")
  helloActor!"hello"
  helloActor!"bjfkds"

  system.shutdown()
}