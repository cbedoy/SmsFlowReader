package sms.app

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SmsViewModel : ViewModel() {

    fun perform(intent : MessageIntent) {
        channel.offer(intent)
    }

    private val channel = Channel<MessageIntent>()

    private val _state = MutableStateFlow<MessageState>(MessageState.IldState)
    val state: StateFlow<MessageState> get() = _state

    var repository: SmsRepository? = null

    init {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {  intent ->
                when(intent){
                    is MessageIntent.LoadMessages -> {
                        repository?.loadMessages?.flowOn(
                            Dispatchers.IO
                        )?.catch {
                            Log.e("ViewModel", "SmsViewModel.catch(${state})")
                        }?.collect {
                            Log.d("ViewModel", "collect(${state})")
                            repository?.loadMessages?.collect {
                                _state.value = it
                            }
                        }
                    }
                }
            }
        }
    }
}