package com.example.furuyonideckmanager2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.furuyonideckmanager2.databinding.ActivityThreeMegamiRegisterBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.launch
import java.util.*

class ThreeMegamiRegisterActivity : AppCompatActivity(), GroupNameDialog.Listener {
    private lateinit var binding: ActivityThreeMegamiRegisterBinding
    private lateinit var realm: Realm;

    // 選択済みのメガミを管理するリスト
    val selectedMegamiList: MutableList<String> = mutableListOf();
    // ダイアログ
    val dialog = GroupNameDialog();

    /**
     * 名前からImageButton取得
     * @param name パーツ名
     * @return 取得したImageButton。取得失敗時はnull。
     */
    fun findImageButtonByName(name: String): ImageButton? {
        val id = resources.getIdentifier(name, "id", packageName);
        if (id == 0) {
            return null;
        }

        return findViewById(id);
    }

    /**
     * メガミのボタンタップ時の処理。
     * @param imageButton タップされたボタン。
     * @param megamiName タップされたメガミの名前。
     */
    fun onButtonTapped(imageButton: ImageButton, megamiName: String) {
        val isPressed = imageButton.isSelected();
        val megami = megamiName.split('_')[0];
        val originImageButton = findImageButtonByName(megami);
        val a1ImageButton = findImageButtonByName(megami + "_a1");
        val a2ImageButton = findImageButtonByName(megami + "_a2");

        originImageButton?.alpha = 0.3f;
        a1ImageButton?.alpha = 0.3f;
        a2ImageButton?.alpha = 0.3f;

        if (!isPressed) {
            // 同種メガミすべてを半透明にする
            originImageButton?.alpha = 0.3f;
            a1ImageButton?.alpha = 0.3f;
            a2ImageButton?.alpha = 0.3f;

            // 枠線を除去する
            originImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a1ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a2ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));

            // 選択状態解除
            originImageButton?.setSelected(false);
            a1ImageButton?.setSelected(false);
            a2ImageButton?.setSelected(false);

            // 枠線を付ける
            imageButton.setBackgroundColor(Color.parseColor("#FFC0BC"));
            // 透明解除
            imageButton.alpha = 1.0f;
            // 同種メガミを間引く
            selectedMegamiList.removeIf { it.contains(megami) }
            // 選択済みリストに追加
            selectedMegamiList.add(megamiName);
        } else {
            // 同種メガミすべての半透明を解除
            originImageButton?.alpha = 1.0f;
            a1ImageButton?.alpha = 1.0f;
            a2ImageButton?.alpha = 1.0f;

            // 枠線を除去する
            originImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a1ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));
            a2ImageButton?.setBackgroundColor(Color.parseColor("#00000000"));

            // 選択状態解除
            originImageButton?.setSelected(false);
            a1ImageButton?.setSelected(false);
            a2ImageButton?.setSelected(false);

            // 選択済みリストから除外
            selectedMegamiList.remove(megamiName);
        }

        // 押下状態を反転させる
        imageButton.setSelected(!isPressed);
        // 作成ボタンの活性状態を変える
        if (selectedMegamiList.size == 3)  {
            binding.startCreationButton.setEnabled(true);
        }
        else {
            binding.startCreationButton.setEnabled(false);
        }
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    fun setClickListeners() {
        binding.yurina.setOnClickListener {onButtonTapped(binding.yurina, "yurina")}
        binding.yurinaA1.setOnClickListener {onButtonTapped(binding.yurinaA1, "yurina_a1")}
        binding.yurinaA2.setOnClickListener {onButtonTapped(binding.yurinaA2, "yurina_a2")}
        binding.himika.setOnClickListener {onButtonTapped(binding.himika, "himika")}
        binding.himikaA1.setOnClickListener {onButtonTapped(binding.himikaA1, "himika_a1")}
        binding.tokoyo.setOnClickListener {onButtonTapped(binding.tokoyo, "tokoyo")}
        binding.tokoyoA1.setOnClickListener {onButtonTapped(binding.tokoyoA1, "tokoyo_a1")}
        binding.tokoyoA2.setOnClickListener {onButtonTapped(binding.tokoyoA2, "tokoyo_a2")}
        binding.oboro.setOnClickListener {onButtonTapped(binding.oboro, "oboro")}
        binding.oboroA1.setOnClickListener {onButtonTapped(binding.oboroA1, "oboro_a1")}
        binding.oboroA2.setOnClickListener {onButtonTapped(binding.oboroA2, "oboro_a2")}
        binding.yukihi.setOnClickListener {onButtonTapped(binding.yukihi, "yukihi")}
        binding.yukihiA1.setOnClickListener {onButtonTapped(binding.yukihiA1, "yukihi_a1")}
        binding.shinra.setOnClickListener {onButtonTapped(binding.shinra, "shinra")}
        binding.shinraA1.setOnClickListener {onButtonTapped(binding.shinraA1, "shinra_a1")}
        binding.saine.setOnClickListener {onButtonTapped(binding.saine, "saine")}
        binding.saineA1.setOnClickListener {onButtonTapped(binding.saineA1, "saine_a1")}
        binding.saineA2.setOnClickListener {onButtonTapped(binding.saineA2, "saine_a2")}
        binding.hagane.setOnClickListener {onButtonTapped(binding.hagane, "hagane")}
        binding.haganeA1.setOnClickListener {onButtonTapped(binding.haganeA1, "hagane_a1")}
        binding.chikage.setOnClickListener {onButtonTapped(binding.chikage, "chikage")}
        binding.chikageA1.setOnClickListener {onButtonTapped(binding.chikageA1, "chikage_a1")}
        binding.kururu.setOnClickListener {onButtonTapped(binding.kururu, "kururu")}
        binding.kururuA1.setOnClickListener {onButtonTapped(binding.kururuA1, "kururu_a1")}
        binding.kururuA2.setOnClickListener {onButtonTapped(binding.kururuA2, "kururu_a2")}
        binding.sariya.setOnClickListener {onButtonTapped(binding.sariya, "sariya")}
        binding.sariyaA1.setOnClickListener {onButtonTapped(binding.sariyaA1, "sariya_a1")}
        binding.utsuro.setOnClickListener {onButtonTapped(binding.utsuro, "utsuro")}
        binding.utsuroA1.setOnClickListener {onButtonTapped(binding.utsuroA1, "utsuro_a1")}
        binding.honoka.setOnClickListener {onButtonTapped(binding.honoka, "honoka")}
        binding.honokaA1.setOnClickListener {onButtonTapped(binding.honokaA1, "honoka_a1")}
        binding.raira.setOnClickListener {onButtonTapped(binding.raira, "raira")}
        binding.rairaA1.setOnClickListener {onButtonTapped(binding.rairaA1, "raira_a1")}
        binding.korunu.setOnClickListener {onButtonTapped(binding.korunu, "korunu")}
        binding.yatsuha.setOnClickListener {onButtonTapped(binding.yatsuha, "yatsuha")}
        binding.yatsuhaA1.setOnClickListener {onButtonTapped(binding.yatsuhaA1, "yatsuha_a1")}
        binding.yatsuhaA2.setOnClickListener {onButtonTapped(binding.yatsuhaA2, "yatsuha_a2")}
        binding.hatsumi.setOnClickListener {onButtonTapped(binding.hatsumi, "hatsumi")}
        binding.hatsumiA1.setOnClickListener {onButtonTapped(binding.hatsumiA1, "hatsumi_a1")}
        binding.mizuki.setOnClickListener {onButtonTapped(binding.mizuki, "mizuki")}
        binding.megumi.setOnClickListener {onButtonTapped(binding.megumi, "megumi")}
        binding.kanae.setOnClickListener {onButtonTapped(binding.kanae, "kanae")}
        binding.kamui.setOnClickListener {onButtonTapped(binding.kamui, "kamui")}
        binding.renri.setOnClickListener {onButtonTapped(binding.renri, "renri")}
        binding.renriA1.setOnClickListener {onButtonTapped(binding.renriA1, "renri_a1")}
        binding.akina.setOnClickListener {onButtonTapped(binding.akina, "akina")}
        binding.sisui.setOnClickListener {onButtonTapped(binding.sisui, "sisui")}
        binding.misora.setOnClickListener {onButtonTapped(binding.misora, "misora")}
        binding.iniru.setOnClickListener {onButtonTapped(binding.iniru, "iniru")}
        binding.iniruA1.setOnClickListener {onButtonTapped(binding.iniruA1, "iniru_a1")}
        binding.iniruA2.setOnClickListener {onButtonTapped(binding.iniruA2, "iniru_a2")}
    }

    /**
     * convertMegamiNameの補助関数。
     * @param engMemiName 英語表記のメガミ名。
     * @return 日本語表記のメガミ名。
     */
    fun convertName(engMegamiName: String): String {
        when(engMegamiName) {
            "yurina" -> return "ユリナ"
            "himika" -> return "ヒミカ"
            "tokoyo" -> return "トコヨ"
            "oboro" -> return "オボロ"
            "yukihi" -> return "ユキヒ"
            "shinra" -> return "シンラ"
            "saine" -> return "サイネ"
            "hagane" -> return "ハガネ"
            "chikage" -> return "チカゲ"
            "kururu" -> return "クルル"
            "sariya" -> return "サリヤ"
            "utsuro" -> return "ウツロ"
            "honoka" -> return "ホノカ"
            "raira" -> return "ライラ"
            "korunu" -> return "コルヌ"
            "yatsuha" -> return "ヤツハ"
            "hatsumi" -> return "ハツミ"
            "mizuki" -> return "ミズキ"
            "megumi" -> return "メグミ"
            "kanae" -> return "カナヱ"
            "kamui" -> return "カムヰ"
            "renri" -> return "レンリ"
            "akina" -> return "アキナ"
            "sisui" -> return "シスイ"
            "misora" -> return "ミソラ"
            "iniru" -> return "イニル"
            else -> return ""
        }
    }

    /**
     * メガミ名を英字から日本語表記へ変換します。
     * 例：yurina_a1 → A1ユリナ
     * @param engMegamiName 英語表記のメガミ名
     * @return 日本語表記のメガミ名を返します。
     */
    fun convertMegamiName(engMemiName: String): String {
        val splitInfo = engMemiName.split('_');
        val megami = splitInfo[0];
        var kind = "";
        if (splitInfo.count() == 2) {
            kind = splitInfo[1];
        }

        // 特殊ケース
        if (megami == "yatsuha" && kind == "a2") {
            return "AAヤツハ";
        } else if (megami == "iniru") {
            if (kind == "a1") {
                return "マヒル"
            } else if (kind == "a2") {
                return "アクル"
            }
            return "イヌル"
        }

        // 通常ケース
        var jpMegami = convertName(megami);
        var prefix = "";
        if (kind == "a1") {
            prefix = "A1";
        } else if (kind == "a2") {
            prefix = "A2";
        }

        return prefix + jpMegami;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityThreeMegamiRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        var config = RealmConfiguration.Builder().allowWritesOnUiThread(true).build();
//        Realm.setDefaultConfiguration(config);
        realm = RealmManager.getRealm()

        // 全メガミボタンに押下時ハンドラ追加
        setClickListeners();

        // 登録画面に遷移するためのボタンにハンドラ追加
        binding.startCreationButton.setOnClickListener {
            /*
            val intent = Intent(this, ThreeMegamiDeckRegisterActivity::class.java);
            // 選ばれたメガミの情報を渡す
            val selectedMegamiArray: Array<String> = selectedMegamiList.toTypedArray()
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
            startActivity(intent);
             */
            val dialogArgs = Bundle();
            val jpMegamiName0 = convertMegamiName(selectedMegamiList[0]);
            val jpMegamiName1 = convertMegamiName(selectedMegamiList[1]);
            val jpMegamiName2 = convertMegamiName(selectedMegamiList[2]);
            dialogArgs.putString("defaultName", jpMegamiName0 + "/" + jpMegamiName1 + "/" + jpMegamiName2);
            dialog.arguments = dialogArgs;
            dialog.show(supportFragmentManager, "register_megamis");
        }
    }

    /**
     * ダイアログの「登録」ボタン押下時の処理。
     */
    override fun confirm()
    {
        // DBに情報を書き込む
        val id = UUID.randomUUID().toString();

        lifecycleScope.launch {
            realm.write {
                copyToRealm(ThreeMegami().apply {
                    this.id = id
                    this.title = dialog.getInput().toString()
                    this.megami0 = selectedMegamiList[0]
                    this.megami1 = selectedMegamiList[1]
                    this.megami2 = selectedMegamiList[2]
                    this.deck01id = ""
                    this.deck12id = ""
                    this.deck20id = ""
                    this.deck01name = ""
                    this.deck12name = ""
                    this.deck20name = ""
                    this.comment = ""
                    this.date = RealmInstant.now()
                })
            }

            Toast.makeText(applicationContext, "三柱を登録しました", Toast.LENGTH_SHORT).show();

            // 画面遷移
            val intent = Intent(this@ThreeMegamiRegisterActivity, ThreeMegamiListActivity::class.java);
            startActivity(intent);
        }
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 何もしない
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