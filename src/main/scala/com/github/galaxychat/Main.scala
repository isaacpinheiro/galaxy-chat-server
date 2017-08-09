package com.github.galaxychat

import java.io.File

import akka.actor._
import com.typesafe.config.ConfigFactory

class Server extends Actor {

  def receive = {
    case _ => // TODO
  }

}

object Main {

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("GalaxyChatServerSystem", config)
    val server = system.actorOf(Props[Server], name = "server")

  }

}
