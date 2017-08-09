package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

case class User(val nick: String, val addr: ActorRef)
case class Connection(val user: User)

class Server extends Actor {

  var users: List[User] = List()

  def receive = {

    case Connection(user) => {
      users = users ++ List(user)
      println("Connection accepted.")
    }

  }

}

object Main {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("GalaxyChatServerSystem", config)
    val server = system.actorOf(Props[Server], name = "server")

    println("\n\nGalaxy Chat Server")
    println("Waiting for incoming connections...")

  }

}
