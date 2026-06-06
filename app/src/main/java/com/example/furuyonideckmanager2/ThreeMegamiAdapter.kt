import SetImageUtil.setImageToImageView
import android.view.View
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.furuyonideckmanager2.ThreeMegami
import com.example.furuyonideckmanager2.databinding.ThreemegamiItemBinding
import kotlinx.coroutines.launch

class ThreeMegamiAdapter(
    private val listener: Listener,
    private val lifecycleOwner: LifecycleOwner
) : ListAdapter<ThreeMegami, ThreeMegamiAdapter.ViewHolder>(ThreeMegamiDiffCallback()) {

    interface Listener {
        fun onGroupDeleteButtonClick(view: View, item: ThreeMegami);
        suspend fun onGroupNameButtonClick(view: View, item: ThreeMegami);
    }

    class ViewHolder(val binding: ThreemegamiItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ThreemegamiItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.showGroupButton.text = item.title

        val context = holder.itemView.context;

        setImageToImageView(item.megami0 + ".jpg", holder.binding.megamiImage0, context.assets);
        setImageToImageView(item.megami1 + ".jpg", holder.binding.megamiImage1, context.assets);
        setImageToImageView(item.megami2 + ".jpg", holder.binding.megamiImage2, context.assets);

        holder.binding.deleteButton.setOnClickListener {
            listener.onGroupDeleteButtonClick(it, item)
        }

        holder.binding.showGroupButton.setOnClickListener {
            lifecycleOwner.lifecycleScope.launch {
                listener.onGroupNameButtonClick(it, item)
            }
        }
    }

    class ThreeMegamiDiffCallback : DiffUtil.ItemCallback<ThreeMegami>() {
        override fun areItemsTheSame(oldItem: ThreeMegami, newItem: ThreeMegami): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ThreeMegami, newItem: ThreeMegami): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.megami0 == newItem.megami0 &&
                    oldItem.megami1 == newItem.megami1 &&
                    oldItem.megami2 == newItem.megami2 &&
                    oldItem.comment == newItem.comment &&
                    oldItem.deck01id == newItem.deck01id &&
                    oldItem.deck12id == newItem.deck12id &&
                    oldItem.deck20id == newItem.deck20id &&
                    oldItem.deck01name == newItem.deck01name &&
                    oldItem.deck12name == newItem.deck12name &&
                    oldItem.deck20name == newItem.deck20name
        }
    }
}