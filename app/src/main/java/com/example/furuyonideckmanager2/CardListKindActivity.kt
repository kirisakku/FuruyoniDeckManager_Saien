package com.example.furuyonideckmanager2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.furuyonideckmanager2.databinding.ActivityCardlistkindBinding

class CardListKindActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCardlistkindBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCardlistkindBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.attack.setOnClickListener {
            val intent = Intent(this, CardListActivity::class.java);
            startActivity(intent);
        }

        binding.action.setOnClickListener {
            val intent = Intent(this, CardKindActivity::class.java);
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