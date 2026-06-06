import SetImageUtil.setImageToImageView
import android.view.View
import com.example.furuyonideckmanager2.Deck
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.example.furuyonideckmanager2.databinding.DeckItemBinding

class DeckAdapter(
    private val listener: Listener
) : ListAdapter<Deck, DeckAdapter.ViewHolder>(DeckDiffCallback()) {

    interface Listener {
        fun onDeleteButtonClick(view: View, item: Deck);
        fun onDeckNameButtonClick(view: View, item: Deck);
    }

    class ViewHolder(val binding: DeckItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DeckItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.showDeckButton.text = item.title

        val context = holder.itemView.context;

        setImageToImageView(item.megami0 + ".jpg", holder.binding.megamiImage0, context.assets)
        setImageToImageView(item.megami1 + ".jpg", holder.binding.megamiImage1, context.assets);

        holder.binding.deleteButton.setOnClickListener {
            listener.onDeleteButtonClick(it, item)
        }

        holder.binding.showDeckButton.setOnClickListener {
            listener.onDeckNameButtonClick(it, item)
        }
    }

    class DeckDiffCallback : DiffUtil.ItemCallback<Deck>() {
        override fun areItemsTheSame(oldItem: Deck, newItem: Deck): Boolean =
            oldItem.fileName == newItem.fileName

        override fun areContentsTheSame(oldItem: Deck, newItem: Deck): Boolean {
            return oldItem.fileName == newItem.fileName &&
                    oldItem.title == newItem.title &&
                    oldItem.megami0 == newItem.megami0 &&
                    oldItem.megami1 == newItem.megami1
        }
    }
}
