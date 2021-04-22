package sms.app

import android.content.Context
import android.database.Cursor
import android.net.Uri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

class SmsRepository(private val context : Context) {
    private val cursor by lazy {
        context.contentResolver.query(
            Uri.parse("content://sms/inbox"),
            null,
            null,
            null,
            null
        )
    }

    val loadMessages
        get() = flow {
            val messages = mutableListOf<Message>()
            if (cursor?.moveToFirst() == true){
                emit(MessageState.Loading(true))
                do {
                    messages.add(Message(
                        message = cursor?.getProp("body"),
                        id = cursor?.getProp("_id")
                    ))
                    emit(MessageState.LoadedSMS(messages.filter {
                        it.message?.contains("", ignoreCase = true) == true // TODO change amazing criteria filter
                    }))
                    delay(200) // Just to make friendly animation
                } while (cursor?.moveToNext() == true)
            }else{
                emit(MessageState.Loading(false))
            }
            emit(MessageState.IldState)
        }
}

private fun Cursor.getProp(prop : String) : String? {
    return getString(getColumnIndex(prop))
}
