package com.example.furuyonideckmanager2

import DeckAdapter2
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.furuyonideckmanager2.databinding.ActivitySelectDeckBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.launch
import java.lang.Exception

class SelectDeckActivity : AppCompatActivity() {
    private lateinit var realm: Realm;
    private var selectedData: Deck? = null;
    private lateinit var binding: ActivitySelectDeckBinding

    /**
     * 画面をリロード。
     */
    private fun reload() {
        val intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);

        startActivity(intent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectDeckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var megami0 = intent.getStringExtra("MEGAMI0");
        var megami1 = intent.getStringExtra("MEGAMI1");
        val uuid = intent.getStringExtra("UUID");
        val target = intent.getStringExtra("TARGET");
        if (megami0 == null || megami1 == null || uuid == null || target == null) {
            throw Exception();
        }

        realm = RealmManager.getRealm()
        binding.itemList.layoutManager = LinearLayoutManager(this);

        val deckList = realm.query<Deck>(
            "(megami0 == $0 AND megami1 == $1) OR (megami0 == $1 AND megami1 == $0)",
            megami0, megami1
        ).sort("date", Sort.DESCENDING)
            .find()

        fun createDeck() {
            val intent = Intent(this, ChooseCardsActivity::class.java);

            // 選ばれたメガミの情報を渡す
            val selectedMegamiArray: Array<String> = arrayOf(megami0, megami1);
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray);
            intent.putExtra("UUID", uuid);
            intent.putExtra("TARGET", target)

            startActivity(intent);
        }

        //　データが無ければ専用の画面にする
        if (deckList.size == 0) {
            binding.noDeckError0.visibility = VISIBLE;
            binding.noDeckError1.visibility = VISIBLE;
            binding.registerDeckButton.visibility = VISIBLE;
            binding.setDeckButton.visibility = INVISIBLE;
            binding.createDeckButton.visibility = INVISIBLE;

            binding.registerDeckButton.setOnClickListener {
                createDeck();
            }

            return;
        }

        // 以下はデータがあるケース
        var targetDeckId = intent.getStringExtra("TARGET_DECKID");
        targetDeckId = if (targetDeckId != null) targetDeckId else "";
        if (targetDeckId != null) {
            selectedData = deckList.find{it.fileName == targetDeckId}
            if (selectedData != null) {
                binding.setDeckButton.isEnabled = true;
            }
        }

        val context = this;
        // アダプタの作成＆設定
        val adapter = DeckAdapter2(deckList, context, targetDeckId);
        adapter.setListener(
            object: DeckAdapter2.Listener {
                override fun onRadioButtonClick(view: View, item: Deck) {
                    selectedData = item;
                    // 設定ボタンの活性化
                    binding.setDeckButton.isEnabled = true;
                }

                override fun onDeckNameButtonClick(view: View, item: Deck) {
                    // 画面遷移
                    val intent = Intent(context, ViewDeckActivity::class.java);

                    // デッキ名を渡す
                    intent.putExtra("DECK_TITLE", item.title);
                    // デッキ名を渡す
                    intent.putExtra("DECK_FILENAME", item.fileName);
                    // メガミ情報を渡す
                    val selectedMegamiArray: Array<String> = arrayOf(item.megami0, item.megami1);
                    intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
                    // この画面固有のフラグ
                    intent.putExtra("EDITABLE", false);

                    startActivity(intent);
                }
            }
        )
        binding.itemList.adapter = adapter;

        binding.setDeckButton.setOnClickListener {
            // DB情報更新
            // 更新対象のデータを検索
            if (selectedData != null) {
                lifecycleScope.launch {
                    DBUtil.registerSelectedDeck(
                        realm,
                        uuid,
                        selectedData!!.fileName,
                        selectedData!!.title,
                        target
                    );
                }
            }
            // 画面遷移
            val newIntent = Intent(applicationContext, ThreeMegamiDeckRegisterActivity::class.java);
            newIntent.putExtra("UUID", uuid);

            startActivity(newIntent);
        }

        binding.createDeckButton.setOnClickListener {
            createDeck();
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