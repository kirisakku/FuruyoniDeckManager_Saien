import SetImageUtil.setImageToImageView
import android.content.Context
import android.view.View
import com.example.furuyonideckmanager2.Deck
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import androidx.recyclerview.widget.RecyclerView
import com.example.furuyonideckmanager2.R

class DeckAdapter2(private val data: List<Deck>, private val context: Context, id: String) :
    RecyclerView.Adapter<DeckAdapter2.ViewHolder>() {

    interface Listener {
        fun onRadioButtonClick(view: View, item: Deck);
        fun onDeckNameButtonClick(view: View, item: Deck);
    }

    private var listener: Listener? = null;
    private var selectedPosition = data.indexOfFirst{it.fileName == id};

    init {
        setHasStableIds(true);
    }

    class ViewHolder(cell: View): RecyclerView.ViewHolder(cell) {
        // パーツを保持しておく場所
        val radioButton: RadioButton = cell.findViewById(R.id.radioButton);
        val megami0: ImageView = cell.findViewById(R.id.megamiImage0);
        val megami1: ImageView = cell.findViewById(R.id.megamiImage1);
        val showDeckButton: Button = cell.findViewById(R.id.showDeckButton);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context);
        val view = inflater.inflate(R.layout.deck_item2, null);
        return ViewHolder(view);
    }

    override fun onBindViewHolder(holder: ViewHolder, pos: Int) {
        val position = holder.adapterPosition;
        val deck: Deck = data.get(position);

        holder.radioButton.isChecked = position == selectedPosition;

        holder.radioButton.setOnClickListener {
            selectedPosition = position;
            notifyDataSetChanged();
            listener?.onRadioButtonClick(it, deck);
        }

        // データ設定
        setImageToImageView(deck.megami0 + ".jpg", holder.megami0, context.resources.assets);
        setImageToImageView(deck.megami1 + ".jpg", holder.megami1, context.resources.assets);
        holder.showDeckButton.text = deck.title;
        holder.showDeckButton.setOnClickListener {
            listener?.onDeckNameButtonClick(it, deck);
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener;
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong();
    }

    override fun getItemViewType(position: Int): Int {
        return position;
    }
}