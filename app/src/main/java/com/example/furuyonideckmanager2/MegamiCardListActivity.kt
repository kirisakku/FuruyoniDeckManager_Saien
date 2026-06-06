package com.example.furuyonideckmanager2

import CsvUtil.convertCsvToStringArray
import CsvUtil.getClassifiedCsvData
import CsvUtil.isAnotherExist
import PartsUtil.*
import SetImageUtil.setImageToImageView
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.furuyonideckmanager2.databinding.ActivityMegamiCardListBinding
import io.realm.kotlin.Realm
import java.io.*
import java.util.*

class MegamiCardListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMegamiCardListBinding
    private lateinit var realm: Realm;

    // 追加札表示ダイアログ
    val additionalCardDialog = AdditionalCardDialogs();
    // 運命カード表示ダイアログ
    val destinyCardDialog = DestinyCardDialog();

    /**
     * 左側のメガミのボタン情報を取得。
     * @return ボタンの種類とボタンのマップの配列を返します。
     */
    fun getMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to binding.card0, "type0" to binding.type00, "type1" to binding.type01),
            mapOf("card" to binding.card1, "type0" to binding.type10, "type1" to binding.type11),
            mapOf("card" to binding.card2, "type0" to binding.type20, "type1" to binding.type21),
            mapOf("card" to binding.card3, "type0" to binding.type30, "type1" to binding.type31),
            mapOf("card" to binding.card4, "type0" to binding.type40, "type1" to binding.type41),
            mapOf("card" to binding.card5, "type0" to binding.type50, "type1" to binding.type51),
            mapOf("card" to binding.card6, "type0" to binding.type60, "type1" to binding.type61),
            mapOf("card" to binding.sCard0, "type0" to binding.sType00, "type1" to binding.sType01),
            mapOf("card" to binding.sCard1, "type0" to binding.sType10, "type1" to binding.sType11),
            mapOf("card" to binding.sCard2, "type0" to binding.sType20, "type1" to binding.sType21),
            mapOf("card" to binding.sCard3, "type0" to binding.sType30, "type1" to binding.sType31),
        );
    }

    /**
     * ボタンの見た目を設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     */
    fun setButtonsView(buttons: Array<Map<String, Button?>>, csvData: List<Map<String, String>>) {
        for (i in csvData.indices) {
            val targetData = csvData[i];
            val targetButtons = buttons[i];
            // カード名設定
            val cardNameButton = targetButtons.get("card");
            cardNameButton?.setText(targetData.get("actionName"));
            // 背景色設定
            if (cardNameButton != null) {
                setButtonBackgroundColor(cardNameButton, targetData)
            }
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData.get("mainType").orEmpty());
            setButtonStyles(targetButtons.get("type1"), targetData.get("subType").orEmpty());
        }
    }

    /**
     * ボタンにハンドラを設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     */
    fun setButtonsHandler(buttons: Array<Map<String, Button?>>, cardCsv: List<Map<String, String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];

            // ハンドラ定義
            val cardButton = targetButtons.get("card");
            // カード名が無かったらハンドラを取り除いて終了
            if (targetData.get("actionName") == "") {
                cardButton?.setOnClickListener(null);
                continue;
            }

            cardButton?.setOnClickListener {
                // 画面遷移
                val intent = Intent(this, ShowCardActivity::class.java);
                // 画像データ
                val image = targetData.get("fileName");
                // 画像データを渡す
                intent.putExtra("IMAGE_FILE_NAME", image);
                startActivity(intent);
            }
        }
    }

    fun setUnselectedAlpha(originImageView: ImageView, a1ImageView: ImageView, a2ImageView: ImageView) {
        originImageView.alpha = 0.25F;
        a1ImageView.alpha = 0.25F;
        a2ImageView.alpha = 0.25F;
    }

    /**
     * メガミimageViewに画像とイベントハンドラを設定。
     * @param imageView 画像とイベントハンドラ設定対象のボタン。
     * @param megamiName メガミ名。
     * @param megamiButtonList メガミのボタン一覧。
     * @param cardCsvList カードのCSVデータ一覧。
     * @param extraCardCsvList 追加札カードのCSVデータ一覧。
     */
    fun setMegamiButton(
        imageView: ImageView,
        megamiName: String,
        megamiButtonList: Array<Map<String, Button?>>,
        cardCsvList: List<Map<String, String>>,
        extraCardCsvList: List<Map<String, String>>
    ) {
        // 画像設定
        setImageToImageView("$megamiName.jpg", imageView, assets);

        // 一律非選択の見た目にするための関数を選択
        var setAlphaFunc = { -> setUnselectedAlpha(binding.megamiImageOrigin, binding.megamiImageA1, binding.megamiImageA2)};

        // ハンドラ設定
        imageView.setOnClickListener {
            // 一律非選択の見た目に設定
            setAlphaFunc();
            // 対象のメガミのalphaを1にして選択の見た目にする
            imageView.alpha = 1F;
            // カード名ボタンの見た目設定
            setButtonsView(megamiButtonList, cardCsvList);
            // カード表示画面に遷移するためのハンドラ設定
            setButtonsHandler(megamiButtonList, cardCsvList);
            // 追加札ボタン設定
            setAdditionalCardButton(binding.additionalCardButton, extraCardCsvList, megamiName);
        }
    }

    /**
     * 追加札ボタンの設定
     * @param additionalButton 追加札一覧ボタン。
     * @param extraCardList 追加札一覧。
     * @param megami メガミ。
     */
    fun setAdditionalCardButton(
        additionalButton: ImageButton,
        extraCardList: List<Map<String, String>>,
        megami: String
    ) {
        if (isDestinyExist(megami)) {
            // 運命ボタンっぽくする
            additionalButton.visibility = VISIBLE
            val blackColor = ContextCompat.getColor(this, android.R.color.black)
            additionalButton.backgroundTintList = ColorStateList.valueOf(blackColor)

            var megamiName = "inuru"
            if (megami == "iniru_a1") {
                megamiName = "mahiru"
            } else if (megami == "iniru_a2") {
                megamiName = "akuru"
            }

            setShowDestinyCardsHandler(additionalButton, megamiName);
            // ### 一旦追加札と運命カードの共存は考えない
            return;
        }

        if (isExtraExist(extraCardList) == false) {
            // 追加札一覧ボタンを非活性
            additionalButton.visibility = INVISIBLE;
        } else {
            // 追加札一覧ボタンを活性化
            additionalButton.visibility = VISIBLE;

            additionalButton.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#D6D7D7"))
            setShowAdditionalCardsHandler(additionalButton, extraCardList);
        }
    }

    /**
     * 追加札一覧の画面を表示するハンドラ
     * @param extraCardCsvList 追加札一覧
     */
    fun showAdditionalCardsHandler(extraCardCsvList: List<Map<String, String>>) {
        val dialogArgs = Bundle();
        dialogArgs.putStringArray("extraCardList", convertCsvToStringArray(extraCardCsvList));
        additionalCardDialog.arguments = dialogArgs;
        additionalCardDialog.show(supportFragmentManager, "additionalCard_dialog");
    }

    /**
     * 追加札ボタンに運命カード表示ハンドラを設定
     * @param additionalCardButton ハンドラ設定対象の追加札ボタン
     * @param megamiName メガミ名
     */
    fun setShowDestinyCardsHandler(
        additionalCardButton: ImageButton,
        megamiName: String,
    ) {
        additionalCardButton.setOnClickListener {
            showDestinyCardsHandler(megamiName);
        }
    }

    /**
     * 運命カード一覧の画面を表示するハンドラ
     * @param megamiName メガミ名
     */
    fun showDestinyCardsHandler(megamiName: String) {
        val dialogArgs = Bundle();
        dialogArgs.putString("megamiName", megamiName)
        destinyCardDialog.arguments = dialogArgs
        destinyCardDialog.show(supportFragmentManager, "destinyCard_dialog")
    }

    /**
     * 追加札ボタンにハンドラを設定
     * @param additionalCardButton ハンドラ設定対象の追加札ボタン
     * @param extraCardList 追加札一覧。
     */
    fun setShowAdditionalCardsHandler(
        additionalCardButton: ImageButton,
        extraCardCsvList: List<Map<String, String>>,
    ) {
        additionalCardButton.setOnClickListener {
            showAdditionalCardsHandler(extraCardCsvList);
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMegamiCardListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realm = RealmManager.getRealm()

        val res = resources;
        val context = applicationContext;

        var megamiName = intent.getStringExtra("CHOSEN_MEGAMI")
        if (megamiName == null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // カード情報の取得
        // オリジン、A-1、A-2に分類されたcsvDataを取得
        val classifiedCardList = getClassifiedCsvData(res.getIdentifier(megamiName, "raw", packageName), res, context, megamiName);
        // オリジン
        val originCardList = classifiedCardList.get("origin");
        // a1
        val a1CardList = classifiedCardList.get("a1");
        // a2
        val a2CardList =classifiedCardList.get("a2");

        // 追加札
        // オリジン
        val originExtraCardList = classifiedCardList.get("extra-origin");
        // a1
        val a1ExtraCardList = classifiedCardList.get("extra-a1");
        // a2
        val a2ExtraCardList = classifiedCardList.get("extra-a2");

        if (originCardList == null) {
            return;
        }

        // ボタン一覧を取得する
        val megamiButtonList = getMegamiCardButtons();

        // 各ボタン初期化処理
        // オリジン
        setMegamiButton(binding.megamiImageOrigin, megamiName, megamiButtonList, originCardList, originExtraCardList!!);

        // A1
        if (isAnotherExist(a1CardList)) {
            setMegamiButton(binding.megamiImageA1, megamiName + "_a1", megamiButtonList, a1CardList!!, a1ExtraCardList!!);
        }

        // A2
        if (isAnotherExist(a2CardList)) {
            setMegamiButton(binding.megamiImageA2, megamiName + "_a2", megamiButtonList, a2CardList!!, a2ExtraCardList!!);
        }

        // 初期状態を設定するためにonClickを実行
        binding.megamiImageOrigin.performClick();
    }

    override fun onDestroy() {
        super.onDestroy();
        RealmManager.closeRealm()
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