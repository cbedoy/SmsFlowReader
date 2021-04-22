package sms.app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sms.app.databinding.MessageViewHolderBinding

class MessageAdapter : ListAdapter<Message, MessageViewHolder>(
    MessageAdapterDiffUtil
) {
    override fun onCreateViewHolder(parent : ViewGroup, viewType : Int) : MessageViewHolder {
        return MessageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.message_view_holder, parent, false)
        )
    }

    override fun onBindViewHolder(holder : MessageViewHolder, position : Int) {
        holder.bind(getItem(position))
    }
}

object MessageAdapterDiffUtil: DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem : Message, newItem : Message) : Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem : Message, newItem : Message) : Boolean {
        return oldItem == newItem
    }

}

class MessageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {

    private val binding by lazy {
        MessageViewHolderBinding.bind(itemView)
    }

    fun bind(message: Message){
        binding.message.text = message.message
        binding.id.text = message.id
    }
}