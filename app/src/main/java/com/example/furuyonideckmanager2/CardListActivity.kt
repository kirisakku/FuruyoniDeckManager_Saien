package com.example.furuyonideckmanager2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.furuyonideckmanager2.databinding.ActivityCardListBinding

class CardListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardListBinding

    /**
     * メガミのボタンタップ時の処理。
     * @param megamiName タップされたメガミの名前。
     */
    fun onButtonTapped(megamiName: String) {
        val intent = Intent(this, MegamiCardListActivity::class.java);
        // 選ばれたメガミの情報を渡す
        intent.putExtra("CHOSEN_MEGAMI", megamiName)
        startActivity(intent);
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    fun setClickListeners() {
        binding.yurina.setOnClickListener {onButtonTapped("yurina")}
        binding.himika.setOnClickListener {onButtonTapped("himika")}
        binding.tokoyo.setOnClickListener {onButtonTapped("tokoyo")}
        binding.oboro.setOnClickListener {onButtonTapped("oboro")}
        binding.yukihi.setOnClickListener {onButtonTapped("yukihi")}
        binding.shinra.setOnClickListener {onButtonTapped("shinra")}
        binding.saine.setOnClickListener {onButtonTapped("saine")}
        binding.hagane.setOnClickListener {onButtonTapped("hagane")}
        binding.chikage.setOnClickListener {onButtonTapped("chikage")}
        binding.kururu.setOnClickListener {onButtonTapped("kururu")}
        binding.sariya.setOnClickListener {onButtonTapped("sariya")}
        binding.utsuro.setOnClickListener {onButtonTapped("utsuro")}
        binding.honoka.setOnClickListener {onButtonTapped("honoka")}
        binding.raira.setOnClickListener {onButtonTapped("raira")}
        binding.korunu.setOnClickListener {onButtonTapped("korunu")}
        binding.yatsuha.setOnClickListener {onButtonTapped("yatsuha")}
        binding.hatsumi.setOnClickListener {onButtonTapped("hatsumi")}
        binding.mizuki.setOnClickListener {onButtonTapped("mizuki")}
        binding.megumi.setOnClickListener {onButtonTapped("megumi")}
        binding.kanae.setOnClickListener {onButtonTapped("kanae")}
        binding.kamui.setOnClickListener {onButtonTapped("kamui")}
        binding.renri.setOnClickListener {onButtonTapped("renri")}
        binding.akina.setOnClickListener {onButtonTapped("akina")}
        binding.sisui.setOnClickListener {onButtonTapped("sisui")}
        binding.misora.setOnClickListener {onButtonTapped("misora")}
        binding.iniru.setOnClickListener {onButtonTapped("iniru")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardListBinding.inflate(layoutInflater);
        setContentView(binding.root)

        // 全メガミボタンに押下時ハンドラ追加
        setClickListeners();
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