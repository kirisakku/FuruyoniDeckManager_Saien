package com.example.furuyonideckmanager2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.example.furuyonideckmanager2.databinding.ActivityRandomChooseBinding
import java.io.IOException
import java.io.InputStream
import kotlin.random.Random

class RandomChooseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRandomChooseBinding

    fun getCheckedMegami(): MutableList<String> {
        val checkedMegami: MutableList<String> = mutableListOf();
        // チェックがされていたら選択可能なメガミ
        if (binding.yurinaCheckbox.isChecked() == true) {
            checkedMegami.add("ユリナ");
        }
        if (binding.saineCheckbox.isChecked() == true) {
            checkedMegami.add("サイネ");
        }
        if (binding.himikaCheckbox.isChecked() == true) {
            checkedMegami.add("ヒミカ");
        }
        if (binding.tokoyoCheckbox.isChecked() == true) {
            checkedMegami.add("トコヨ");
        }
        if (binding.oboroCheckbox.isChecked() == true) {
            checkedMegami.add("オボロ");
        }
        if (binding.yukihiCheckbox.isChecked() == true) {
            checkedMegami.add("ユキヒ");
        }
        if (binding.shinraCheckbox.isChecked() == true) {
            checkedMegami.add("シンラ");
        }
        if (binding.haganeCheckbox.isChecked() == true) {
            checkedMegami.add("ハガネ");
        }
        if (binding.chikageCheckbox.isChecked() == true) {
            checkedMegami.add("チカゲ");
        }
        if (binding.kururuCheckbox.isChecked() == true) {
            checkedMegami.add("クルル");
        }
        if (binding.sariyaCheckbox.isChecked() == true) {
            checkedMegami.add("サリヤ");
        }
        if (binding.rairaCheckbox.isChecked() == true) {
            checkedMegami.add("ライラ");
        }
        if (binding.utsuroCheckbox.isChecked() == true) {
            checkedMegami.add("ウツロ");
        }
        if (binding.honokaCheckbox.isChecked() == true) {
            checkedMegami.add("ホノカ");
        }
        if (binding.korunuCheckbox.isChecked() == true) {
            checkedMegami.add("コルヌ");
        }
        if (binding.yatsuhaCheckbox.isChecked() == true) {
            checkedMegami.add("ヤツハ");
        }
        if (binding.hatsumiCheckbox.isChecked() == true) {
            checkedMegami.add("ハツミ");
        }
        if (binding.mizukiCheckbox.isChecked() == true) {
            checkedMegami.add("ミズキ");
        }
        if (binding.megumiCheckbox.isChecked() == true) {
            checkedMegami.add("メグミ")
        }
        if (binding.kanaeCheckbox.isChecked() == true) {
            checkedMegami.add("カナヱ")
        }
        if (binding.kamuiCheckbox.isChecked() == true) {
            checkedMegami.add("カムヰ")
        }
        if (binding.renriCheckbox.isChecked() == true) {
            checkedMegami.add("レンリ")
        }
        if (binding.akinaCheckbox.isChecked() == true) {
            checkedMegami.add("アキナ")
        }
        if (binding.sisuiCheckbox.isChecked() == true) {
            checkedMegami.add("シスイ")
        }
        if (binding.misoraCheckbox.isChecked() == true) {
            checkedMegami.add("ミソラ")
        }
        if (binding.iniruCheckbox.isChecked() == true) {
            checkedMegami.add("イニル")
        }

        return checkedMegami;
    }

    fun getNinzuu(selected: String): Int {
        if (selected == "2人") {
            return 2;
        }
        if (selected == "3人") {
            return 3;
        }
        if (selected == "1人") {
            return 1;
        }

        // 想定外
        return -1;
    }

    fun setText(list: MutableList<Map<String, String>>, selected: String) {
        if (selected == "2人") {
            binding.megami1.text = list[0].get("megamiCategory");
            binding.megami3.text = list[1].get("megamiCategory");
        } else if (selected == "3人") {
            binding.megami1.text = list[0].get("megamiCategory");
            binding.megami2.text = list[1].get("megamiCategory");
            binding.megami3.text = list[2].get("megamiCategory");
        } else if (selected == "1人") {
            binding.megami2.text = list[0].get("megamiCategory");
        }
    }

    fun setImage(list: MutableList<Map<String, String>>, selected: String) {
        val assets = resources.assets;

        var instream1: InputStream? = null;
        var instream2: InputStream? = null;
        var instream3: InputStream? = null;
        var bitmap1: Bitmap? = null;
        var bitmap2: Bitmap? = null;
        var bitmap3: Bitmap? = null;
        // try-with-resources
        try {
            if (selected == "2人") {
                instream1 = assets.open(list[0].get("image").toString());
                instream3 = assets.open(list[1].get("image").toString());
            } else if (selected == "3人") {
                instream1 = assets.open(list[0].get("image").toString());
                instream2 = assets.open(list[1].get("image").toString());
                instream3 = assets.open(list[2].get("image").toString());
            } else if (selected == "1人") {
                instream2 = assets.open(list[0].get("image").toString());
            }

            if (instream1 != null) {
                bitmap1 = BitmapFactory.decodeStream(instream1);
                binding.megamiImage1.setImageBitmap(bitmap1);
            }
            if (instream2 != null) {
                bitmap2 = BitmapFactory.decodeStream(instream2);
                binding.megamiImage2.setImageBitmap(bitmap2);
            }
            if (instream3 != null) {
                bitmap3 = BitmapFactory.decodeStream(instream3);
                binding.megamiImage3.setImageBitmap(bitmap3);
            }
        } catch (e: IOException) {
            e.printStackTrace();
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRandomChooseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 全チェック状態を管理するグローバル変数
        var checked = false;

        binding.chooseButton.setOnClickListener {
            val selected = binding.ninzuu.selectedItem as String;
            val num = this.getNinzuu(selected);
            val checkedMegami = this.getCheckedMegami();

            // 初期化
            binding.megami1.text = "";
            binding.megami2.text = "";
            binding.megami3.text = "";
            binding.megamiImage1.setImageBitmap(null);
            binding.megamiImage2.setImageBitmap(null);
            binding.megamiImage3.setImageBitmap(null);

            // メガミが人数分選べないケース
            if (checkedMegami.size < num) {
                binding.errorMessage.text = "エラー：メガミを" + selected + "選べません";
            } else {
                // 以下はメガミが人数分選べるケース
                val choosenMegamiList: MutableList<Map<String, String>> = mutableListOf();
                val megamiList = getList(this, checkedMegami);
                var nonDuplicatedMegamiList = megamiList.toList();
                binding.errorMessage.text = "";

                for (i in 1..num) {
                    val randomNumber = Random.nextInt(nonDuplicatedMegamiList.size);
                    val targetData = nonDuplicatedMegamiList[randomNumber];
                    choosenMegamiList.add(targetData)

                    // 同じ種類のメガミを除外する
                    nonDuplicatedMegamiList =
                        nonDuplicatedMegamiList.filter { elem ->
                            elem.get("megamiName") != targetData.get(
                                "megamiName"
                            )
                        }
                }

                // テキスト設定
                this.setText(choosenMegamiList, selected);

                // 画像設定
                this.setImage(choosenMegamiList, selected);
            }
        }

        binding.allButton.setOnClickListener {
            // 全メガミの選択状態を設定する
            binding.yurinaCheckbox.setChecked(checked);
            binding.saineCheckbox.setChecked(checked);
            binding.himikaCheckbox.setChecked(checked);
            binding.tokoyoCheckbox.setChecked(checked);
            binding.oboroCheckbox.setChecked(checked);
            binding.yukihiCheckbox.setChecked(checked);
            binding.shinraCheckbox.setChecked(checked);
            binding.haganeCheckbox.setChecked(checked);
            binding.chikageCheckbox.setChecked(checked);
            binding.kururuCheckbox.setChecked(checked);
            binding.sariyaCheckbox.setChecked(checked);
            binding.rairaCheckbox.setChecked(checked);
            binding.utsuroCheckbox.setChecked(checked);
            binding.honokaCheckbox.setChecked(checked);
            binding.korunuCheckbox.setChecked(checked);
            binding.yatsuhaCheckbox.setChecked(checked);
            binding.hatsumiCheckbox.setChecked(checked);
            binding.mizukiCheckbox.setChecked(checked);
            binding.megumiCheckbox.setChecked(checked);
            binding.kanaeCheckbox.setChecked(checked);
            binding.kamuiCheckbox.setChecked(checked);
            binding.renriCheckbox.setChecked(checked);
            binding.akinaCheckbox.setChecked(checked);
            binding.sisuiCheckbox.setChecked(checked);
            binding.misoraCheckbox.setChecked(checked);
            binding.iniruCheckbox.setChecked(checked);

            // フラグを反転
            checked = !checked;

            // テキストを変更
            val text = if (checked) "全選択" else "全選択解除";
            binding.allButton.setText(text)
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