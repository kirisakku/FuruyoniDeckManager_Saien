package com.example.furuyonideckmanager2

import ThreeMegamiAdapter
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
import com.example.furuyonideckmanager2.databinding.ActivityThreeMegamiListBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.isValid
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class DeleteGroupConfirmDialog: DialogFragment() {
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

        val deckName = arguments?.getString("GROUP_TITLE");
        builder.setMessage("「$deckName」を削除します。よろしいですか？");

        builder.setPositiveButton("削除") {_, _ ->
            lifecycleScope.launch {
                val id = arguments?.getString("UUID");
                if (id != null) {
                    listener?.confirm(id);
                }
            }
        }
        builder.setNegativeButton("キャンセル") {_, _ ->
            listener?.cancel();
        }
        return builder.create();
    }
}


class ThreeMegamiListActivity : AppCompatActivity(), DeleteGroupConfirmDialog.Listener {
    private lateinit var binding: ActivityThreeMegamiListBinding
    private lateinit var realm: Realm;

    /**
     * ダイアログの「削除」ボタン押下時の処理。
     * @param id 対象データのUUID。
     */
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun confirm(id: String) {
        val targetGroup = realm.query<ThreeMegami>("id == $0", id).first().find()
        if (targetGroup != null) {
            realm.write {
                val liveObject = findLatest(targetGroup)
                if (liveObject != null && liveObject.isValid()) {
                    delete(liveObject)
                }
            }
        }
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
        binding.registerGroupButton.visibility = VISIBLE;
        binding.registerGroupButton.setOnClickListener {
            val intent = Intent(this, ThreeMegamiRegisterActivity::class.java);
            startActivity(intent);
        }
    }

    /**
     * 削除されたデッキデータのクリーンアップ処理。
     * @param targetData 開こうとするデータ
     */
    /**
     * 削除されたデッキデータのクリーンアップ処理。
     * @param targetData 開こうとするデータ
     */
    suspend fun cleanUpDeletedData(targetData: ThreeMegami) {
        realm.write {
            val managedTarget = findLatest(targetData)
            if (managedTarget == null) return@write

            fun clearIfDeckMissing(deckId: String, clearId: (String) -> Unit, clearName: (String) -> Unit) {
                if (deckId.isNotBlank()) {
                    val deckExists = query<Deck>("fileName == $0", deckId).find().isNotEmpty()
                    if (!deckExists) {
                        clearId("")
                        clearName("")
                    }
                }
            }

            clearIfDeckMissing(
                managedTarget.deck01id,
                { managedTarget.deck01id = it },
                { managedTarget.deck01name = it }
            )
            clearIfDeckMissing(
                managedTarget.deck12id,
                { managedTarget.deck12id = it },
                { managedTarget.deck12name = it }
            )
            clearIfDeckMissing(
                managedTarget.deck20id,
                { managedTarget.deck20id = it },
                { managedTarget.deck20name = it }
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivityThreeMegamiListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realm = RealmManager.getRealm()
        binding.itemList.layoutManager = LinearLayoutManager(this);

        val adapter = ThreeMegamiAdapter(object: ThreeMegamiAdapter.Listener {
            override fun onGroupDeleteButtonClick(view: View, item: ThreeMegami) {
                val dialog = DeleteGroupConfirmDialog()
                val dialogArgs = Bundle().apply {
                    putString("GROUP_TITLE", item.title)
                    putString("UUID", item.id)
                }

                dialog.arguments = dialogArgs

                dialog.show(supportFragmentManager, "deletegroup_dialog")
            }

            override suspend fun onGroupNameButtonClick(view: View, item: ThreeMegami) {
                val targetData = realm.query<ThreeMegami>("id == $0", item.id).first().find()

                if (targetData == null) {
                    throw Exception()
                }

                // 開いた先でデッキが削除されていないかを確認し、削除されている場合は削除
                cleanUpDeletedData(targetData)

                val intent = Intent(this@ThreeMegamiListActivity, ThreeMegamiDeckRegisterActivity::class.java)

                intent.putExtra("UUID", item.id)

                startActivity(intent)
            }
        }, this)

        binding.itemList.adapter = adapter;

        // Flowで監視
        lifecycleScope.launch {
            realm.query<ThreeMegami>()
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