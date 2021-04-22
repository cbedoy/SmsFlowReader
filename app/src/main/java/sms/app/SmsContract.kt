package sms.app

sealed class MessageState{
    object IldState: MessageState()
    data class Loading(val start: Boolean):MessageState()
    data class LoadedSMS(val messages: List<Message>): MessageState()
}

sealed class MessageIntent{
    object LoadMessages: MessageIntent()
}

data class Message(
    val id: String? = null,
    val address: String? = null,
    val message: String? = null,
    val readState: String? = null,
    val time: String? = null,
    val type: String? = null,
    val sender: String? = null
)