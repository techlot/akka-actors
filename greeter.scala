import akka.actor.{Actor, ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}

class Greeter extends Actor {
  val log: LoggingAdapter = Logging(context.system, this)

  override def receive: Receive = {
    case "Anna" => log.info("Hi Anna!")
    case "Tom" => log.info("Hi Tom!")
    case _ => log.info("Hi stranger!")
  }
}

object Greeter_Test extends App {
  val system = ActorSystem("MySystem")
  val greeter = system.actorOf(Props[Greeter], name = "Greeter")

  greeter ! "Anna"
  greeter ! "Tom"
  greeter ! "Kate"    // stranger

  Thread.sleep(1000);
  system.terminate();
}
