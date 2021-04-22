package sms.app

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kotlinpermissions.KotlinPermissions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sms.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val messageAdapter = MessageAdapter()

    private val viewModel by lazy {
        ViewModelProvider(this).get(SmsViewModel::class.java).apply {
            repository = SmsRepository(
                context = this@MainActivity
            )
        }
    }

    private val binding  by lazy {
        ActivityMainBinding.inflate(LayoutInflater.from(this))
    }

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val linearLayoutManager = LinearLayoutManager(this)

        with(binding.recyclerView){
            adapter = messageAdapter
            layoutManager = linearLayoutManager
            addItemDecoration(
                DividerItemDecoration(context, linearLayoutManager.orientation)
            )
        }

        lifecycleScope.launch {
            viewModel.state.collect {  state ->
                when(state){
                    is MessageState.IldState -> {

                    }
                    is MessageState.LoadedSMS -> {
                        messageAdapter.submitList(state.messages)
                    }
                    is MessageState.Loading -> {
                        binding.swipeRefreshLayout.isRefreshing = state.start
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        KotlinPermissions.with(this).permissions(
            Manifest.permission.READ_SMS
        ).onAccepted {
            viewModel.perform(MessageIntent.LoadMessages)
        }.ask()
    }
}