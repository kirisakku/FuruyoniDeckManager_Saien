package com.example.furuyonideckmanager2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.furuyonideckmanager2.databinding.ActivityCardkindBinding

class CardKindActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardkindBinding

    /**
     * カード種類のボタンタップ時の処理。
     * @param megamiName タップされたカードの種類。
     */
    fun onButtonTapped(cardKind: String) {
        val intent = Intent(this, KindListActivity::class.java);
        // 選ばれたカードの種類の情報を渡す
        intent.putExtra("CHOSEN_KIND", cardKind);
        startActivity(intent);
    }

    /**
     * ボタンタップ時の処理を設定。
     */
    fun setClickListeners() {
        binding.attack.setOnClickListener {onButtonTapped("attack")}
        binding.action.setOnClickListener {onButtonTapped("action")}
        binding.assignment.setOnClickListener {onButtonTapped("assignment")}
        binding.handling.setOnClickListener {onButtonTapped("handling")}
        binding.fullpower.setOnClickListener {onButtonTapped("fullpower")}
        binding.indefinite.setOnClickListener {onButtonTapped("indefinite")}
        binding.nohandling.setOnClickListener {onButtonTapped("nohandling")}
        binding.distance.setOnClickListener {onButtonTapped("distance")}
        binding.arrow.setOnClickListener {onButtonTapped("arrow")}
        binding.life.setOnClickListener {onButtonTapped("life")}
        binding.hyphen.setOnClickListener {onButtonTapped("hyphen")}
        binding.buff.setOnClickListener {onButtonTapped("buff")}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardkindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 全種類ボタンに押下時ハンドラ追加
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