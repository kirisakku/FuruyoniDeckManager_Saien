package com.example.furuyonideckmanager2

import CsvUtil.removeCsvFile
import DeckAdapter
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.VISIBLE
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furuyonideckmanager2.databinding.ActivityDeckListBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isValid
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class DeleteConfirmDialog: DialogFragment() {
    interface Listener {
        suspend fun confirm(csvName: String);
        fun cancel();
    }
    private var listener: Listener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        when (context) {
            is Listener -> listener = context;
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());
        // 表示内容設定
        val deckName = arguments?.getString("DECK_TITLE");

        builder.setMessage("デッキ「$deckName」を削除します。よろしいですか？");

        builder.setPositiveButton("削除") {_, _ ->
            lifecycleScope.launch {
                val csvName = arguments?.getString("DECK_CSV");
                if (csvName != null) {
                    listener?.confirm(csvName);
                }
            }
        }

        builder.setNegativeButton("キャンセル") {_, _ ->
            listener?.cancel();
        }
        return builder.create();
    }
}


class DeckListActivity : AppCompatActivity(), DeleteConfirmDialog.Listener {
    private lateinit var binding: ActivityDeckListBinding

    private lateinit var realm: Realm;

    /**
     * ダイアログの「削除」ボタン押下時の処理。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun confirm(csvName: String) {
        // デッキリストから対象行を削除
        // デッキリストから対象行を削除
        val targetDeck = realm.query<Deck>("fileName == $0", csvName).first().find()
        if (targetDeck != null) {
            realm.write {
                val liveObject = findLatest(targetDeck)
                if (liveObject != null && liveObject.isValid()) {
                    delete(liveObject)
                }
            }
        }

        // csvファイルを削除
        removeCsvFile(csvName, applicationContext);
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 何もしない
    }

    /**
     * データが無い時の画面を生成。
     */
    fun createEmptyView() {
        binding.noDeckError0.visibility = VISIBLE;
        binding.noDeckError1.visibility = VISIBLE;
        binding.registerDeckButton.visibility = VISIBLE;
        binding.registerDeckButton.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java);
            startActivity(intent);
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeckListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realm = RealmManager.getRealm()
        binding.itemList.layoutManager = LinearLayoutManager(this);

        val adapter = DeckAdapter(object: DeckAdapter.Listener {
            override fun onDeleteButtonClick(view: View, item: Deck) {
                val dialog = DeleteConfirmDialog()
                val dialogArgs = Bundle().apply {
                    putString("DECK_TITLE", item.title)
                    putString("DECK_CSV", item.fileName)
                }
                dialog.arguments = dialogArgs
                dialog.show(supportFragmentManager, "delete_dialog")
            }

            override fun onDeckNameButtonClick(view: View, item: Deck) {
                val intent = Intent(this@DeckListActivity, ViewDeckActivity::class.java)
                intent.putExtra("DECK_TITLE", item.title)
                intent.putExtra("DECK_FILENAME", item.fileName)
                val selectedMegamiArray = arrayOf(item.megami0, item.megami1)
                intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
                startActivity(intent)
            }
        })

        binding.itemList.adapter = adapter;

        // Flowで監視
        lifecycleScope.launch {
            realm.query<Deck>()
                .sort("date", io.realm.kotlin.query.Sort.DESCENDING)
                .asFlow()
                .collect { results ->
                    val items = results.list.map { it.copy() }
                    adapter.submitList(items)

                    if (items.isEmpty()) {
                        createEmptyView()
                    }
                }
        }
    }

    override fun onDestroy() {
        super.onDestroy();
        RealmManager.closeRealm()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu);
        return true;
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId) {
            R.id.home -> {
                val intent = Intent(this, MainActivity::class.java);
                startActivity(intent);
            }
        }
        return super.onOptionsItemSelected(item)
    }
}