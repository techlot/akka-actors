import akka.actor.{Props, ActorSystem, Actor}


class Ping extends Actor {
  var points = 0                                                              // in the beginning ping has 0 points

  def hit(): Unit = {                                                         // function that displays who hits the ball
    val random_num = scala.util.Random.nextInt()                              // and calculates points for ping
    if (random_num % 2 == 0) {                                                // some random number generated. If it's divisible
      println(self.path.name + "[" + points + "]" + ": ping ")                // by 2, player hits the ball.
      points += 1
    } else {
      println(self.path.name + "[" + points + "]" + ": *miss*")               // otherwise misses.
    }
  }

  override def receive: Receive = {
    case "start" => hit()                                                     // if ping receives command to start - it hits the ball first
      sender ! "hit_from_ping"                                                // passes the ball to pong

    case "hit_from_pong" => hit()                                             // if ball received from pong - hits back to pong
      if (points == 10) {                                                     // if ping reaches 10 points
        println(self.path.name + "[" + points + "]" + ": Won!")               // message that player won displayed
        sender ! "you_lost"                                                   // message that pong lost sent to pong
        context.stop(self)                                                    // ping stops to play
      }
      else {
        sender ! "hit_from_ping"                                              // if ping didn't reach 10 points yet,
      }                                                                       // ball sent back to pong

    case "you_lost" => context.stop(self)                                     // if message about lost from pong was received,
      println(self.path.name + ": Ok, " + sender.path.name + " won.")         // ping stops playing.
  }
}

class Pong extends Actor {
  var points = 0                                                              // in the beginning pong has 0 points

  def hit(): Unit = {                                                         // function that displays who hits the ball
    val random_num = scala.util.Random.nextInt()                              // and calculates points for pong
    if (random_num % 2 == 0) {                                                // some random number generated. If it's divisible
      println(self.path.name + "[" + points + "]" + ": pong ")                // by 2, player hits the ball.
      points += 1
    } else {
      println(self.path.name + "[" + points + "]" + ": *miss*")               // otherwise misses.
    }
  }

  override def receive: Receive = {
    case "start" => hit()                                                     // if ping receives command to start - it hits the ball first
      sender ! "hit_from_pong"                                                // passes the ball to pong

    case "hit_from_ping" => hit()
      if (points == 10) {                                                     // if pong reaches 10 points
        println(self.path.name + "[" + points + "]" + ": Won!")               // message that player won displayed
        sender ! "you_lost"                                                   // message that ping lost send
        context.stop(self)                                                    // pong stops to play
      }
      else {
        sender ! "hit_from_pong"                                              // if pong didn't reach 10 points yet,
      }                                                                       // ball sent back to ping

    case "you_lost" => context.stop(self)                                     // if message about lost from ping was received,
      println(self.path.name + ": Ok, " + sender.path.name + " won.")         // pong stops playing.
  }
}

object Ping_Pong_Test extends App {
  val system = ActorSystem("MySystem")
  val Karen = system.actorOf(Props[Ping], name = "Karen")
  val Steve = system.actorOf(Props[Pong], name = "Steve")

  Karen.tell("start", Steve)                                                  // if Steve told Karen to start
  //Steve.tell("start", Karen)                                                // if Karen told Steve to start
}
