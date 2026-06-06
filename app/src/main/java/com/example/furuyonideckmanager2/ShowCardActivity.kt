package com.example.furuyonideckmanager2

import SetImageUtil.setImageToImageView
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.furuyonideckmanager2.databinding.ActivityShowCardBinding

class ShowCardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityShowCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShowCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 画像データの取得
        var image = intent.getStringExtra("IMAGE_FILE_NAME")
        if (image != null) {
            // 画像の設定
            setImageToImageView(image, binding.cardImage, assets);
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