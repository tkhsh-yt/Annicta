package tkhshyt.annicta.layout.message

import android.content.Context
import android.widget.Toast

class ToastMessageCreator : MessageCreator {

    override fun create(): MessageBuilder {
        return ToastMessageBuilder()
    }
}

class ToastMessageBuilder : MessageBuilder {

    var context: Context? = null
    var message: String = ""
    var duration: Message.Duration = Message.Duration.LONG
    var type: Message.Type = Message.Type.NOTIFICATION

    override fun context(context: Context?): MessageBuilder {
        this.context = context
        return this
    }

    override fun message(message: String): MessageBuilder {
        this.message = message
        return this
    }

    override fun duration(duration: Message.Duration): MessageBuilder {
        this.duration = duration
        return this
    }

    override fun type(type: Message.Type): MessageBuilder {
        this.type = type
        return this
    }

    override fun build(): Message {
        return ToastMessage(context, message, duration, type)
    }

}

class ToastMessage(override val context: Context?, override val message: String, override val duration: Message.Duration, override val type: Message.Type) : Message {

    override fun show() {
        val length = if (duration == Message.Duration.LONG) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        Toast.makeText(context, message, length).show()
    }
}