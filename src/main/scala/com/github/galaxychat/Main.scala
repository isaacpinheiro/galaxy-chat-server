package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

case class User(val nick: String, val addr: ActorRef)
case class Connection(val user: User)
case class Message(val nick: String, val content: String)
case class Response(val content: String)
case object ListUsers
case class Nick(val oldNick: String, val newNick: String)
case class Quit(val nick: String)

class Server extends Actor {

  var users: List[User] = List()

  def receive = {

    case Connection(user) => {
      users = users ++ List(user)
      println("Connection accepted.")
    }

    case Message(nick, content) => {
      val msg = nick + ": " + content
      users.foreach((u: User) => { u.addr ! Response(msg + "\n") })
    }

    case ListUsers => {

      val msg = "\nUsers:\n" + users.foldLeft("")((res: String, u: User) => {
        res + u.nick + "\n"
      })

      sender ! Response(msg)

    }

    case Nick(oldNick, newNick) => {
      users = users.map((u: User) => {
        if (u.nick == oldNick) {
          User(newNick, u.addr)
        } else {
          u
        }
      })
    }

    case Quit(nick) => {
      users = users.foldLeft(List[User]())((res: List[User], u: User) => {
        if (u.nick == nick) {
          res
        } else {
          res ++ List(u)
        }
      })
    }

  }

}

object Main {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("GalaxyChatServerSystem", config)
    val server = system.actorOf(Props[Server], name = "server")

    println("\nGalaxy Chat Server")
    println("Waiting for incoming connections...")

  }

}
