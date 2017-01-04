package com.ru.waka

import akka.actor.ActorSystem
import com.typesafe.config.ConfigFactory
import slack.SlackUtil
import slack.rtm.SlackRtmClient


object Main {
  val config = ConfigFactory.load()
  val token = config.getString("slack.token")
  implicit val system = ActorSystem("slack")
  implicit val ec = system.dispatcher

  val client = SlackRtmClient(token)
  val selfId = client.state.self.id

  def main(args: Array[String]) {
    client.onMessage { message =>
      val mentionedIds = SlackUtil.extractMentionedIds(message.text)

      if(mentionedIds.contains(selfId)) {
        client.sendMessage(message.channel, s"<@${message.user}>: Hey!")
      }
    }
  }
}