package com.example.furuyonideckmanager2

import SetImageUtil.setImageToImageView
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.furuyonideckmanager2.databinding.ActivityThreeMegamiDeckRegisterBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

class ThreeMegamiDeckRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThreeMegamiDeckRegisterBinding
    private lateinit var realm: Realm;

    /**
     * 登録ボタンを押された時のハンドラです。
     * @param megami0 対象メガミ0。
     * @param megami1 対象メガミ1。
     * @param uuid UUID。
     * @param target ターゲット。01, 12, 20のいずれか。
     * @param targetDeckId ターゲットに対して設定されているデッキのUUID。
     */
    fun onRegisterButtonTapped(megami0: String, megami1: String, uuid: String, target: String, targetDeckId: String) {
        // デッキ選択画面に遷移
        val intent = Intent(this, SelectDeckActivity::class.java);

        // メガミ名を渡す
        intent.putExtra("MEGAMI0", megami0);
        intent.putExtra("MEGAMI1", megami1);
        intent.putExtra("UUID", uuid);
        intent.putExtra("TARGET", target);
        intent.putExtra("TARGET_DECKID", targetDeckId);

        startActivity(intent);
    }

    /**
     * 参照ボタンを押された時のハンドラです。
     * @param megami0 対象メガミ0。
     * @param megami1 対象メガミ1。
     * @param deckId デッキのID。
     * @param deckName デッキの名称。
     */
    fun onViewButtonTapped(megami0: String, megami1: String, deckId: String, deckName: String) {
        // デッキ参照画面に遷移
        val intent = Intent(this, ViewDeckActivity::class.java);

        // データ受け渡し
        val selectedMegamiArray: Array<String> = arrayOf(megami0, megami1);
        intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray);
        intent.putExtra("DECK_FILENAME", deckId);
        intent.putExtra("DECK_TITLE", deckName);
        // この画面固有のフラグ
        intent.putExtra("EDITABLE", false);

        startActivity(intent);
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityThreeMegamiDeckRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realm = RealmManager.getRealm()

        // UUID
        val uuid = intent.getStringExtra("UUID");
        if (uuid == null) {
            throw Exception();
        }

        val targetData = realm.query<ThreeMegami>("id == $0", uuid).first().find()

        if (targetData == null) {
            throw Exception();
        }

        val megami0 = targetData.megami0;
        val megami1 = targetData.megami1;
        val megami2 = targetData.megami2;

        // メガミ画像をセット
        setImageToImageView(megami0 + ".jpg", binding.megamiImage0, assets);
        setImageToImageView(megami0 + ".jpg", binding.megamiImage00, assets);
        setImageToImageView(megami0 + ".jpg", binding.megamiImage20, assets);

        setImageToImageView(megami1 + ".jpg", binding.megamiImage1, assets);
        setImageToImageView(megami1 + ".jpg", binding.megamiImage01, assets);
        setImageToImageView(megami1 + ".jpg", binding.megamiImage11, assets);

        setImageToImageView(megami2 + ".jpg", binding.megamiImage2, assets);
        setImageToImageView(megami2 + ".jpg", binding.megamiImage12, assets);
        setImageToImageView(megami2 + ".jpg", binding.megamiImage22, assets);

        // デッキ情報セット
        val deck01 = targetData.deck01id;
        val deck12 = targetData.deck12id;
        val deck20 = targetData.deck20id;
        val deck01Name = targetData.deck01name;
        val deck12Name = targetData.deck12name;
        val deck20Name = targetData.deck20name;

        // 既にデッキが選択済み場合はデッキ名を書き換える
        if (deck01Name != "") {
            binding.viewDeck0.text = deck01Name;
            binding.viewDeck0.isEnabled = true;
            binding.registerDeck0.text = deck01Name;
            binding.registerDeck0.setBackgroundResource(R.color.gray);
        }
        if (deck12Name != "") {
            binding.viewDeck1.text = deck12Name;
            binding.viewDeck1.isEnabled = true;
            binding.registerDeck1.text = deck12Name;
            binding.registerDeck1.setBackgroundResource(R.color.gray);
        }
        if (deck20Name != "") {
            binding.viewDeck2.text = deck20Name;
            binding.viewDeck2.isEnabled = true;
            binding.registerDeck2.text = deck20Name;
            binding.registerDeck2.setBackgroundResource(R.color.gray);
        }

        // ハンドラ設定
        binding.registerDeck0.setOnClickListener{onRegisterButtonTapped(megami0, megami1, uuid, "01", deck01)};
        binding.registerDeck1.setOnClickListener{onRegisterButtonTapped(megami1, megami2, uuid, "12", deck12)};
        binding.registerDeck2.setOnClickListener{onRegisterButtonTapped(megami2, megami0, uuid, "20", deck20)};

        binding.viewDeck0.setOnClickListener{onViewButtonTapped(megami0, megami1, deck01, deck01Name)}
        binding.viewDeck1.setOnClickListener{onViewButtonTapped(megami1, megami2, deck12, deck12Name)}
        binding.viewDeck2.setOnClickListener{onViewButtonTapped(megami2, megami0, deck20, deck20Name)}
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