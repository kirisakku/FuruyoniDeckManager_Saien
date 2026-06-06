package PartsUtil

import CsvUtil.isAnother
import CsvUtil.isSpecialCard
import android.app.ActionBar
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.furuyonideckmanager2.R

/**
 * メインタイプ/サブタイプのボタンのスタイルを設定。
 * @param button スタイルを設定する対象のボタン。
 * @param type メインタイプ/サブタイプの属性。
 */
fun setButtonStyles(button: Button?, type: String) {
    button?.setVisibility(View.VISIBLE);

    when(type) {
        "攻撃" -> {
            button?.setBackgroundResource(R.drawable.circle_red);
            button?.setText("攻");
        };
        "行動" -> {
            button?.setBackgroundResource(R.drawable.circle_blue)
            button?.setText("行");
        };
        "付与" -> {
            button?.setBackgroundResource(R.drawable.circle_green)
            button?.setText("付");
        };
        "全力" -> {
            button?.setBackgroundResource(R.drawable.circle_yellow)
            button?.setText("全");
        };
        "対応" -> {
            button?.setBackgroundResource(R.drawable.circle_purple)
            button?.setText("対");
        };
        "不定" -> {
            button?.setBackgroundResource(R.drawable.circle_gray)
            button?.setText("不")
        }
        else -> {
            button?.setVisibility(View.INVISIBLE);
        }
    }
}

/**
 * カード名ボタンに背景色を設定。
 * @param button ボタン。
 * @param cardData カードデータ。
 * @param changeAColor アナザーのカードの背景色を変えるかどうか。デフォルトはtrue。
 * @param changeExColor 追加札の背景色を変えるかどうか。デフォルトはtrue。
 */
fun setButtonBackgroundColor(button: Button, cardData: Map<String, String>, changeAColor: Boolean = true, changeExColor: Boolean = true) {
    // アナザーのカードかつ追加札でない場合は桃色を設定
    if (isAnother(cardData) && cardData.get("actionName") != "" && cardData.get("no")?.startsWith('A') == false && changeAColor) {
        button.setBackgroundColor(Color.parseColor("#ffe4e1"));
    } else if(cardData.get("no") == "A-E" && changeExColor)  {
        // 追加札の場合は薄紫色
        button.setBackgroundColor((Color.parseColor("#e6e6fa")))
    } else {
        // それ以外の場合は通常札であれば灰色、切札の場合は金色
        val color = if (isSpecialCard(cardData)) "#eee8aa" else "#d6d7d7";
        button.setBackgroundColor((Color.parseColor(color)));
    }
}

/**
 * 追加札が存在するかどうかを判定。
 * @param extraCardList 追加札のカードリスト。
 * @return 追加札が存在するならtrue、存在しないならfalse。
 */
fun isExtraExist(extraCardList: List<Map<String, String>>?): Boolean {
    return extraCardList != null && extraCardList?.count() != 0;
}

/**
 * 運命カードが存在するかどうかを判定。
 * @param megamiName メガミ名。
 * @return 運命カードが存在するならtrue、存在しないならfalse。
 */
fun isDestinyExist(megamiName: String): Boolean {
    if (megamiName.startsWith("iniru")) {
        return true;
    }
    return false;
}

/**
 * dpをpixelに変換。
 * @param dp 変換対象のdp。
 * @param context 呼び出し元のcontext。
 */
fun convertDpToPixel(dp: Int, context: Context): Int {
    val density = context.resources.displayMetrics.density;
    val result = ((dp * density) + 0.5).toInt();
    return result;
}

/**
 * ボタンのレイアウト設定。
 * @param button レイアウト設定済みのボタン
 * @param context 呼び出し元のcontext。
 */
fun setLayoutParamsToButton(button: Button, context:Context) {
    button.layoutParams = ActionBar.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    ).apply {
        width = convertDpToPixel(230, context);
        height = convertDpToPixel(30, context);
        gravity = Gravity.CENTER_VERTICAL;
        marginEnd = convertDpToPixel(8, context);
        topMargin = convertDpToPixel(8, context);
        bottomMargin = convertDpToPixel(8, context);
        marginStart = convertDpToPixel(8, context);
    }
    button.setPadding(0, 0, 0, 0);
}

/**
 * 属性を表すボタンを生成。
 * @param type 属性
 * @param context 呼び出し元のcontext。
 * @return 属性を表すボタン
 */
fun createTypeButton(type: String, context: Context): Button {
    val typeButton = Button(context);
    setButtonStyles(typeButton, type);
    typeButton.textSize = 15.0f;
    typeButton.setPadding(0, 0, 0, 0);
    typeButton.layoutParams = ActionBar.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    ).apply {
        width = convertDpToPixel(20, context);
        height = convertDpToPixel(20, context);
        gravity = Gravity.CENTER;
        marginEnd = convertDpToPixel(8, context);
    }

    return typeButton;
}

/**
 * 属性を表すボタンを生成。（追加カード用）
 * @param type 属性
 * @param context 呼び出し元のcontext。
 * @return 属性を表すボタン
 */
fun createTypeButtonForAdditionalCard(type: String, context: Context): Button {
    val typeButton = Button(context);
    setButtonStyles(typeButton, type);
    typeButton.textSize = 15.0f;
    typeButton.setPadding(0, 0, 0, 0);
    typeButton.layoutParams = LinearLayout.LayoutParams(
        convertDpToPixel(20, context),
        convertDpToPixel(20, context)
    ).apply {
        gravity = Gravity.CENTER;
        marginEnd = convertDpToPixel(8, context);
    }

    return typeButton;
}

/**
 * ImageViewのレイアウト設定。
 * @param imageView ImageView。
 * @param context 呼び出し元のcontext。
 */
fun setLayoutParamsToImageView(imageView: ImageView, context:Context) {
    imageView.layoutParams = ActionBar.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    ).apply {
        width = convertDpToPixel(30, context);
        height = convertDpToPixel(30, context);
        gravity = Gravity.CENTER_VERTICAL;
        marginEnd = convertDpToPixel(8, context);
        topMargin = convertDpToPixel(8, context);
        bottomMargin = convertDpToPixel(8, context);
        marginStart = convertDpToPixel(8, context);
    }
    imageView.setPadding(0, 0, 0, 0);
}