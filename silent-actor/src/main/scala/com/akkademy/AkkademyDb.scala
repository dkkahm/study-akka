package com.akkademy

import akka.actor.Actor
import akka.event.Logging
import com.akkademy.message.SetRequest

import scala.collection.mutable

class AkkademyDb extends Actor {
  val map = new mutable.HashMap[String, AnyRef]
  val log = Logging(context.system, this)
  override def receive: Receive = {
    case SetRequest(key, value) => {
      log.info("received SetRequest - key: {}, value: {}", key, value)
      map.put(key, value)
    }
    case o => log.info("received unknown message: {}", o)
  }
}
