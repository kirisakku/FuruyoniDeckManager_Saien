package com.example.furuyonideckmanager2

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.furuyonideckmanager2.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    // 起動モード
    var mode = "";

    // 選択済みのメガミを管理するリスト
    val selectedMegamiList: MutableList<String> = mutableListOf();

    /**
     * メガミのボタンタップ時の処理。
     * @param imageButton タップされたボタン。
     * @param megamiName タップされたメガミの名前。
     */
    fun onButtonTapped(imageButton: ImageButton, megamiName: String) {
        val isPressed = imageButton.isSelected();
        if (!isPressed) {
            // 枠線を付ける
            imageButton.setBackgroundColor(Color.parseColor("#FFC0BC"));
            // 選択済みリストに追加
            selectedMegamiList.add(megamiName);
        } else {
            // 枠線を除去する
            imageButton.setBackgroundColor(Color.parseColor("#00000000"));
            // 選択済みリストから除外
            selectedMegamiList.remove(megamiName);
        }

        // 押下状態を反転させる
        imageButton.setSelected(!isPressed);
        // 作成ボタンの活性状態を変える
        // デッキ登録の場合
        if (selectedMegamiList.size == 2 && mode == "registerDeck") {
            binding.startCreationButton.setEnabled(true);
        } else if (selectedMegamiList.size == 3 && mode == "threeMegami")  {
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
        binding.himika.setOnClickListener {onButtonTapped(binding.himika, "himika")}
        binding.tokoyo.setOnClickListener {onButtonTapped(binding.tokoyo, "tokoyo")}
        binding.oboro.setOnClickListener {onButtonTapped(binding.oboro, "oboro")}
        binding.yukihi.setOnClickListener {onButtonTapped(binding.yukihi, "yukihi")}
        binding.shinra.setOnClickListener {onButtonTapped(binding.shinra, "shinra")}
        binding.saine.setOnClickListener {onButtonTapped(binding.saine, "saine")}
        binding.hagane.setOnClickListener {onButtonTapped(binding.hagane, "hagane")}
        binding.chikage.setOnClickListener {onButtonTapped(binding.chikage, "chikage")}
        binding.kururu.setOnClickListener {onButtonTapped(binding.kururu, "kururu")}
        binding.sariya.setOnClickListener {onButtonTapped(binding.sariya, "sariya")}
        binding.utsuro.setOnClickListener {onButtonTapped(binding.utsuro, "utsuro")}
        binding.honoka.setOnClickListener {onButtonTapped(binding.honoka, "honoka")}
        binding.raira.setOnClickListener {onButtonTapped(binding.raira, "raira")}
        binding.korunu.setOnClickListener {onButtonTapped(binding.korunu, "korunu")}
        binding.yatsuha.setOnClickListener {onButtonTapped(binding.yatsuha, "yatsuha")}
        binding.hatsumi.setOnClickListener {onButtonTapped(binding.hatsumi, "hatsumi")}
        binding.mizuki.setOnClickListener {onButtonTapped(binding.mizuki, "mizuki")}
        binding.megumi.setOnClickListener {onButtonTapped(binding.megumi, "megumi")}
        binding.kanae.setOnClickListener {onButtonTapped(binding.kanae, "kanae")}
        binding.kamui.setOnClickListener {onButtonTapped(binding.kamui, "kamui")}
        binding.renri.setOnClickListener {onButtonTapped(binding.renri, "renri")}
        binding.akina.setOnClickListener {onButtonTapped(binding.akina, "akina")}
        binding.sisui.setOnClickListener {onButtonTapped(binding.sisui, "sisui")}
        binding.misora.setOnClickListener {onButtonTapped(binding.misora, "misora")}
        binding.inuru.setOnClickListener {onButtonTapped(binding.inuru, "iniru")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // モード情報の取得
        val modeData = intent.getStringExtra("MODE");
        mode = if (modeData != null) modeData else "registerDeck";

        // モードが三柱選択の場合はボタンのテキストを変える
        if (mode == "threeMegami") {
            binding.startCreationButton.setText("三柱登録");
        }

        // 全メガミボタンに押下時ハンドラ追加
        setClickListeners();

        // 登録画面に遷移するためのボタンにハンドラ追加
        binding.startCreationButton.setOnClickListener {
            val intent = Intent(this, ChooseCardsActivity::class.java);
            // 選ばれたメガミの情報を渡す
            val selectedMegamiArray: Array<String> = selectedMegamiList.toTypedArray()
            intent.putExtra("CHOSEN_MEGAMI", selectedMegamiArray)
            startActivity(intent);
        }
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