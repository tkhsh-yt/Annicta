package tkhshyt.annicta.layout.message

import android.content.Context

interface Message {

    val context: Context?
    val message: String
    val duration: Duration
    val type: Type

    fun show()

    enum class Duration {
        LONG, SHORT;
    }

    enum class Type {
        NOTIFICATION,
        ALERT,
        CONFIRM
    }
}

interface MessageCreator {

    fun create(): MessageBuilder
}

interface MessageBuilder {

    fun context(context: Context?): MessageBuilder

    fun message(message: String): MessageBuilder

    fun duration(duration: Message.Duration): MessageBuilder

    fun type(type: Message.Type): MessageBuilder

    fun build(): Message
}

