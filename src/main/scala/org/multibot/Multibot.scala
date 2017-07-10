package org.multibot

import java.nio.charset.Charset

import org.pircbotx.Configuration.Builder
import org.pircbotx._
import org.pircbotx.hooks.ListenerAdapter
import org.pircbotx.hooks.events.{MessageEvent, PrivateMessageEvent}
import org.pircbotx.hooks.types.GenericMessageEvent

case class Msg(channel: String, sender: String, message: String)
object Cmd {
  def unapply(s: String) = if (s.contains(' ')) Some(s.split(" ", 2).toList) else None
}

case class Multibot(
   inputSanitizer: String => String,
   outputSanitizer: String => Array[String],
   cache: InterpretersCache,
   botname: String,
   channels: List[String],
   settings: Builder => Builder = identity) {

  val LAMBDABOT = "lambdabot"
  val ADMINS = List("imeredith", "lopex", "tpolecat", "OlegYch")
  val httpHandler = HttpHandler()
  def start() {
    new Thread() {
      override def run(): Unit = bot.startBot()
      start()
    }
    new Thread() {
      override def run(): Unit = {
        var connected = false
        while (true) {
          Thread.sleep(10000)
          if (connected) {
            channels.foreach(c => DieOn.exception(bot.getUserChannelDao.getChannel(c)))
          } else {
            connected = channels.forall(c => bot.getUserChannelDao.containsChannel(c))
          }
        }
      }
      start()
    }
  }

  private val builder = settings(new Builder().
    setName(botname).setEncoding(Charset.forName("UTF-8")).setAutoNickChange(true).setAutoReconnect(true)
    .setShutdownHookEnabled(false)
    .setAutoSplitMessage(true)
    .addListener(new ListenerAdapter {
    def handle(_e: (String, String, GenericMessageEvent)) = {
      val (channel, sender, e) = _e
      def sendLines(channel: String, message: String) = {
        println(message)

        outputSanitizer(message).foreach(m => {
            if (channel == sender) e.respond(m)
            else e.getBot[PircBotX].getUserChannelDao.getChannel(channel).send().message(m)
        })
      }
      def interpreters = InterpretersHandler(cache, httpHandler, sendLines, inputSanitizer)
      def admin = AdminHandler(e.getBot[PircBotX].getNick + ":", ADMINS, _ => (), _ => (), sendLines) //todo
      DieOn.error {
        val msg = Msg(channel, sender, e.getMessage)
        interpreters.serve(msg)
        admin.serve(msg)
      }
    }
    override def onPrivateMessage(event: PrivateMessageEvent): Unit = {
      super.onPrivateMessage(event)
      handle(event.getUser.getNick, event.getUser.getNick, event)
    }
    override def onMessage(event: MessageEvent): Unit = {
      super.onMessage(event)
      handle(event.getChannel.getName, event.getUser.getNick, event)
    }
  }))
  channels.foreach(builder.addAutoJoinChannel)
  private object bot extends org.pircbotx.PircBotX(builder.buildConfiguration()) {
    override def startBot(): Unit = {
      try {
        super.startBot()
      } catch {
        case e: java.io.IOException =>
          e.printStackTrace()
          Thread.sleep(1000)
          startBot()
      }
    }
  }
}
