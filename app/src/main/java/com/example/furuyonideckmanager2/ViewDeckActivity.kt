package com.example.furuyonideckmanager2

import CsvUtil.getClassifiedCsvData
import CsvUtil.readInternalFile
import PartsUtil.setButtonStyles
import SetImageUtil.setImageToImageView
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.furuyonideckmanager2.databinding.ActivityViewDeckBinding
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.launch

class EditConfirmDialog: DialogFragment() {
    interface Listener {
        fun confirm(csvName: String);
        fun cancel();
    }
    private var listener: Listener? = null;

    override fun onAttach(context: Context) {
        super.onAttach(context);
        when (context) {
            is Listener -> listener = context;
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());

        val deckName = arguments?.getString("DECK_TITLE");
        val fileName = arguments?.getString("DECK_FILENAME");
        val chosenMegami = arguments?.getStringArray("CHOSEN_MEGAMI");
        val data = arguments?.getStringArray("DECK_DATA");

        builder.setMessage("デッキ「$deckName」を編集します。よろしいですか？");

        builder.setPositiveButton("編集") {_, _ ->
            val intent = Intent(context, ChooseCardsActivity::class.java);

            // データの受け渡し
            intent.putExtra("DECK_FILENAME", fileName);
            intent.putExtra("CHOSEN_MEGAMI", chosenMegami);
            intent.putExtra("DECK_DATA", data);
            intent.putExtra("DECK_NAME", deckName);

            // 編集画面に画面遷移
            startActivity(intent);
        }
        builder.setNegativeButton("キャンセル") {_, _ ->
            listener?.cancel();
        }
        return builder.create();
    }
}

class ViewDeckActivity : AppCompatActivity(), CommentDialog.Listener {
    private lateinit var binding: ActivityViewDeckBinding
    private lateinit var realm: Realm;
    val dialog = CommentDialog();

    override fun update() {
        // 画面のコメント部分を更新
        binding.comment.setText(dialog.getInput());

        // DBを更新
        // 更新対象のデータを検索
        var deckCSV = intent.getStringExtra("DECK_FILENAME");

        lifecycleScope.launch {
            realm.write {
                val targetData = this.query<Deck>("fileName == $0", deckCSV).first().find()
                targetData?.let {
                    it.comment = dialog.getInput().toString()
                }
            }
        }
    }

    /**
     * ダイアログの「キャンセル」ボタン押下時の処理。
     */
    override fun cancel() {
        // 処理なし
    }

    /**
     * 左側のメガミのボタン情報を取得。
     * @return ボタンの種類とボタンのマップの配列を返します。
     */
    fun getLeftMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to binding.megami0Card0View, "type0" to binding.megami0Type00View, "type1" to binding.megami0Type01View),
            mapOf("card" to binding.megami0Card1View, "type0" to binding.megami0Type10View, "type1" to binding.megami0Type11View),
            mapOf("card" to binding.megami0Card2View, "type0" to binding.megami0Type20View, "type1" to binding.megami0Type21View),
            mapOf("card" to binding.megami0Card3View, "type0" to binding.megami0Type30View, "type1" to binding.megami0Type31View),
            mapOf("card" to binding.megami0Card4View, "type0" to binding.megami0Type40View, "type1" to binding.megami0Type41View),
            mapOf("card" to binding.megami0Card5View, "type0" to binding.megami0Type50View, "type1" to binding.megami0Type51View),
            mapOf("card" to binding.megami0Card6View, "type0" to binding.megami0Type60View, "type1" to binding.megami0Type61View),
            mapOf("card" to binding.megami0SCard0View, "type0" to binding.megami0SType00View, "type1" to binding.megami0SType01View),
            mapOf("card" to binding.megami0SCard1View, "type0" to binding.megami0SType10View, "type1" to binding.megami0SType11View),
            mapOf("card" to binding.megami0SCard2View, "type0" to binding.megami0SType20View, "type1" to binding.megami0SType21View),
            mapOf("card" to binding.megami0SCard3View, "type0" to binding.megami0SType30View, "type1" to binding.megami0SType31View)
        );
    }

    /**
     * 右側のメガミのボタン情報を取得。
     * @return ボタンの種類とボタンのマップの配列を返します。
     */
    fun getRightMegamiCardButtons(): Array<Map<String, Button?>> {
        return arrayOf(
            mapOf("card" to binding.megami1Card0View, "type0" to binding.megami1Type00View, "type1" to binding.megami1Type01View),
            mapOf("card" to binding.megami1Card1View, "type0" to binding.megami1Type10View, "type1" to binding.megami1Type11View),
            mapOf("card" to binding.megami1Card2View, "type0" to binding.megami1Type20View, "type1" to binding.megami1Type21View),
            mapOf("card" to binding.megami1Card3View, "type0" to binding.megami1Type30View, "type1" to binding.megami1Type31View),
            mapOf("card" to binding.megami1Card4View, "type0" to binding.megami1Type40View, "type1" to binding.megami1Type41View),
            mapOf("card" to binding.megami1Card5View, "type0" to binding.megami1Type50View, "type1" to binding.megami1Type51View),
            mapOf("card" to binding.megami1Card6View, "type0" to binding.megami1Type60View, "type1" to binding.megami1Type61View),
            mapOf("card" to binding.megami1SCard0View, "type0" to binding.megami1SType00View, "type1" to binding.megami1SType01View),
            mapOf("card" to binding.megami1SCard1View, "type0" to binding.megami1SType10View, "type1" to binding.megami1SType11View),
            mapOf("card" to binding.megami1SCard2View, "type0" to binding.megami1SType20View, "type1" to binding.megami1SType21View),
            mapOf("card" to binding.megami1SCard3View, "type0" to binding.megami1SType30View, "type1" to binding.megami1SType31View)
        );
    }

    /**
     * ボタンの見た目を設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     * @param chosenCardCsv 選ばれたカードのcsvデータ
     */
    fun setButtonsView(buttons: Array<Map<String, Button?>>, cardCsv: List<Map<String, String>>, chosenCardCsv: List<List<String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];
            // カード名設定
            targetButtons.get("card")?.setText(targetData.get("actionName"));
            // 色変更
            setButtonStyles(targetButtons.get("type0"), targetData.get("mainType").orEmpty());
            setButtonStyles(targetButtons.get("type1"), targetData.get("subType").orEmpty());
            // 活性か非活性か（リストにあれば活性）
            // TODO: ここの処理は別関数に分けたほうが綺麗な気がする
            val isEnable = chosenCardCsv.any{it[1] == targetData.get("actionName")}
            targetButtons.get("card")?.isEnabled = isEnable;
            if (isEnable == false) {
                targetButtons.get("card")?.alpha = 0.75F;
                targetButtons.get("type0")?.alpha = 0.25F;
                targetButtons.get("type1")?.alpha = 0.25F;
            }
        }
    }

    /**
     * ボタンにハンドラを設定。
     * @param buttons ボタン配列。
     * @param cardCsv カードのcsvデータ。
     * @oaran context コンテキスト。
     */
    fun setButtonsHandler(buttons: Array<Map<String, Button?>>, cardCsv: List<Map<String, String>>) {
        for (i in cardCsv.indices) {
            val targetData = cardCsv[i];
            val targetButtons = buttons[i];

            // ハンドラ定義
            val cardButton = targetButtons.get("card");
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

    /**
     * コメントにハンドラを設定。
     */
    fun setCommentHandler() {
        binding.comment.setOnClickListener {
            val dialogArgs = Bundle();
            val comment = binding.comment.text.toString();
            dialogArgs.putString("comment", comment);
            dialog.arguments = dialogArgs;
            dialog.show(supportFragmentManager, "comment_dialog");
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);

        binding = ActivityViewDeckBinding.inflate(layoutInflater)
        setContentView(binding.root)

        realm = RealmManager.getRealm()

        val res = resources;
        val context = applicationContext;

        // データの取り出し
        var deckTitle = intent.getStringExtra("DECK_TITLE");
        var chosenMegami = intent.getStringArrayExtra("CHOSEN_MEGAMI");
        var fileName = intent.getStringExtra("DECK_FILENAME");
        Log.d("Debug", "***** FILE NAME ${fileName} *****")
        var editable = intent.getBooleanExtra("EDITABLE", true);

        // タイトル設定
        binding.deckName.setText(deckTitle);
        // 画像設定
        val megami0 = chosenMegami?.get(0);
        val megami1 = chosenMegami?.get(1);
        if (megami0 == null || megami1 ==null) {
            // TODO: 後でエラー処理書きたい
            return;
        }

        // メガミ画像の設定
        setImageToImageView(megami0 + ".jpg", binding.megamiImage0View, assets);
        setImageToImageView(megami1 + ".jpg", binding.megamiImage1View, assets);

        // カード情報の取得
        val splitedName0 = megami0.split('_');
        val splitedName1 = megami1.split('_');
        // オリジナルのメガミ名を取得
        val originMegamiName0 = splitedName0[0];
        val originMegamiName1 = splitedName1[0];
        // オリジン、A-1、A-2に分類されたcsvDataを取得
        val classifiedCardList0 = getClassifiedCsvData(res.getIdentifier(originMegamiName0, "raw", packageName), res, context, originMegamiName0);
        val classifiedCardList1 = getClassifiedCsvData(res.getIdentifier(originMegamiName1, "raw", packageName), res, context, originMegamiName1);
        // 対応するカードリストを取得
        var cardList0 = classifiedCardList0.get("origin");
        var cardList1 = classifiedCardList1.get("origin");
        if (splitedName0.count() > 1) {
            cardList0 = classifiedCardList0.get(splitedName0[1]);
        }
        if (splitedName1.count() > 1) {
            cardList1 = classifiedCardList1.get(splitedName1[1]);
        }

        if (cardList0 == null || cardList1 == null) {
            return;
        }

        var deckCardList: List<List<String>> = listOf();
        // csvファイルからデッキ情報を読み込む
        if (fileName != null) {
            deckCardList = readInternalFile(fileName, applicationContext);
        }

        // 画面の初期化
        // ボタン一覧を取得する
        val megamiButtonList0 = getLeftMegamiCardButtons();
        val megamiButtonList1 = getRightMegamiCardButtons();
        // ボタンの見た目設定
        setButtonsView(megamiButtonList0, cardList0, deckCardList);
        setButtonsView(megamiButtonList1, cardList1, deckCardList);

        // カード表示画面に遷移するためのハンドラ設定
        setButtonsHandler(megamiButtonList0, cardList0);
        setButtonsHandler(megamiButtonList1, cardList1);

        val targetData = realm.query<Deck>("fileName == $0", fileName).first().find()
        binding.comment.setText(targetData?.comment);
        // コメント部分にコメント編集用のハンドラを設定
        if (editable) {
            setCommentHandler();
        }

        // editable = falseの時は鉛筆アイコンは非表示
        if (editable == false) {
            binding.editButton.visibility = View.INVISIBLE;
        }

        // 編集ボタンのハンドラ設定
        binding.editButton?.setOnClickListener {
            val dialog = EditConfirmDialog();
            // データ受け渡し
            val dialogArgs = Bundle();
            dialogArgs.putString("DECK_TITLE", deckTitle);
            dialogArgs.putString("DECK_FILENAME", fileName);
            dialogArgs.putStringArray("CHOSEN_MEGAMI", chosenMegami);
            val data = deckCardList.map{elem -> elem.joinToString(",")}.toTypedArray();
            dialogArgs.putStringArray("DECK_DATA", data);

            dialog.arguments = dialogArgs;
            // ダイアログ表示
            dialog.show(supportFragmentManager, "edit_dialog");
        }
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