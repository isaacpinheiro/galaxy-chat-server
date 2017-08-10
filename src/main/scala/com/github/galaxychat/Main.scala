package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

case class User(val nick: String, val addr: ActorRef)
case class Connection(val user: User)
case class Message(val nick: String, val content: String)
case class Response(val content: String)
case class ListUsers(val nick: String)

class Server extends Actor {

  var users: List[User] = List()

  def receive = {

    case Connection(user) => {
      users = users ++ List(user)
      println("Connection accepted.")
    }

    case Message(nick, content) => {
      val msg = nick + ": " + content
      users.foreach((u: User) => { u.addr ! Response("\n" + msg + "\n") })
    }

    case ListUsers(nick) => {

      val msg = "\nUsers:\n" + users.foldLeft("")((res: String, u: User) => {
        res + u.nick + "\n"
      })

      val addr = users.filter((u: User) => u.nick == nick).head.addr
      addr ! Response(msg)

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
