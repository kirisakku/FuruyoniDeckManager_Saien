package com.example.furuyonideckmanager2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle
import com.example.furuyonideckmanager2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    var instance: MainActivity? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.attack.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java);
            startActivity(intent);
        }

        binding.action.setOnClickListener {
            val intent = Intent(this, DeckListActivity::class.java);
            startActivity(intent);
        }

        binding.threeMegamiRegister.setOnClickListener {
            val intent = Intent(this, ThreeMegamiRegisterActivity::class.java);
            startActivity(intent);
        }

        binding.threeMegamiView.setOnClickListener {
            val intent = Intent(this, ThreeMegamiListActivity::class.java);
            startActivity(intent);
        }

        binding.cardList.setOnClickListener {
            val intent = Intent(this, CardListKindActivity::class.java);
            startActivity(intent);
        }

        binding.randomChoose.setOnClickListener {
            val intent = Intent(this, RandomChooseActivity::class.java);
            startActivity(intent);
        }

        instance = this;
    }
}