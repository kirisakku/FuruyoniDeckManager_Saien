package com.example.furuyonideckmanager2

import CsvUtil.getAllCardData
import PartsUtil.createTypeButton
import PartsUtil.setButtonBackgroundColor
import PartsUtil.setLayoutParamsToButton
import PartsUtil.setLayoutParamsToImageView
import SetImageUtil.setImageToImageView
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.furuyonideckmanager2.databinding.ActivityKindlistBinding

class KindListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityKindlistBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKindlistBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cardKind = intent.getStringExtra("CHOSEN_KIND")
        if (cardKind == null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        val context = applicationContext;

        // カード情報の取得
        val allCards = getAllCardData(resources, packageName, context);

        var filterResult: List<Map<String, String>> = listOf();
        // カテゴリでフィルタ
        when (cardKind) {
            "attack" -> filterResult = allCards.filter{ it.get("mainType") == "攻撃" };
            "action" -> filterResult = allCards.filter { it.get("mainType") == "行動" }
            "assignment" -> filterResult = allCards.filter { it.get("mainType") == "付与" }
            "handling" -> filterResult = allCards.filter { it.get("subType") == "対応" }
            "fullpower" -> filterResult = allCards.filter { it.get("subType") == "全力" }
            "indefinite" -> filterResult = allCards.filter { it.get("mainType") == "不定" }
            "nohandling" -> filterResult = allCards.filter { it.get("nohandling") == "y" }
            "distance" -> filterResult = allCards.filter { it.get("distance") == "y" }
            "arrow" -> filterResult = allCards.filter { it.get("arrow") == "y" }
            "life" -> filterResult = allCards.filter { it.get("life") == "y" }
            "hyphen" -> filterResult = allCards.filter { it.get("hyphen") == "y" }
            "buff" -> filterResult = allCards.filter { it.get("buff") == "y" }
        }

        for(i in filterResult.indices) {
            var elem = filterResult[i];

            // 横整列レイアウト作成
            val hLinearLayout = LinearLayout(context);

            // ImageView追加
            val imageView = ImageView(context);
            val suffix = if(elem.get("type") == "a1") "_a1" else if (elem.get("type") == "a2") "_a2" else "";
            setImageToImageView(elem.get("megamiName") + suffix + ".jpg", imageView, context.resources.assets);
            // ImageViewのレイアウト設定
            setLayoutParamsToImageView(imageView, context);

            // 名称ボタン追加
            val button = Button(context);
            // アナザーのカードの背景色を変えない
            val changeAColor = false;
            // 追加札の背景色を変えない
            val changeExColor = false;
            setButtonBackgroundColor(button, elem, changeAColor, changeExColor);
            button.setText(elem.get("actionName"));
            // ボタンのレイアウト設定
            setLayoutParamsToButton(button, context);

            // ハンドラ設定
            button?.setOnClickListener {
                // 画面遷移
                val intent = Intent(context, ShowCardActivity::class.java);
                // 画像データ
                val image = elem.get("fileName");
                // 画像データを渡す
                intent.putExtra("IMAGE_FILE_NAME", image);
                startActivity(intent);
            }

            // 属性ボタン追加
            val typeButton0 = createTypeButton(elem.get("mainType").orEmpty(), context);
            val typeButton1 = createTypeButton(elem.get("subType").orEmpty(), context)

            // 作成した要素を横整列
            hLinearLayout.addView(imageView);
            hLinearLayout.addView(button);
            hLinearLayout.addView(typeButton0);
            hLinearLayout.addView(typeButton1);
            // 中央寄せにする
            hLinearLayout.gravity = Gravity.CENTER;

            // 作成した横整列レイアウトを縦整列レイアウトに足す
            binding.vLinearLayout.addView(hLinearLayout);
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