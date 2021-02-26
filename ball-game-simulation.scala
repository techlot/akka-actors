import akka.actor.{Actor, ActorRef, ActorSystem, Props}

case class Ball(count: Int)

class Player(num: Int, players: Array[ActorRef]) extends Actor {
  override def receive: Receive = {
    case Ball(count) =>
      var next_player = scala.util.Random.nextInt(players.length)                                // generates random number from 0-2 for choosing the next player.
      while (next_player == num) next_player = scala.util.Random.nextInt(players.length)         // makes sure next player is not same as current player.
      println("Player [" + (num + 1) + "]" + " passes to player [" + (next_player + 1) + "] ball #" + (count + 1))  // prints out each stage of passing the ball between players,
      // for a nice view, num + 1, so our players are 1-3, not 0-2.
      Thread.sleep(900)                                                                   // since there is no limit of throws, for a smooth output .sleep(), so this way our
      players(next_player) ! Ball(count + 1)                                                     // output doesn't blow up (not necessary, but I thought adding it would be nice in this particular case).
  }                                                                                              // message to the next, randomly picked player, sent.
}

object Game_Test extends App {
  val system = ActorSystem("MySystem")                                                           // we have 3 players in out game.
  val num_of_players = 3                                                                         // empty array with length 3 created.
  val players = new Array[ActorRef](num_of_players)
  for (i <- 0 until num_of_players) {                                                            // we could fill array by putting each player to the array manually.
    players(i) = system.actorOf(Props(classOf[Player], i, players))                              // but, if we would have more than 3 players, it would be better to
  }                                                                                              // use loop instead, so that, it will fill our array of players itself.
  players(0) ! Ball(0)                                                                           // game starts from player 1.
}
