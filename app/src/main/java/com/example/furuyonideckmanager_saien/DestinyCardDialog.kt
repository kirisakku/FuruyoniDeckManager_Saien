package com.example.furuyonideckmanager_saien

import PartsUtil.*
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ScrollView
import androidx.fragment.app.DialogFragment

class DestinyCardDialog: DialogFragment() {
    interface Listener {
        fun cancel();
    }
    private var listener: Listener? = null;
    private var isFront: Boolean = true;
    private var megamiName: String? = "";

    override fun onAttach(context: Context) {
        super.onAttach(context);
        when (context) {
            is Listener -> listener = context;
        }
    }

    fun setData(dialogLayout: View) {
        val card0 = dialogLayout.findViewById<ImageView>(R.id.destiny0)
        val card1 = dialogLayout.findViewById<ImageView>(R.id.destiny1)
        val card2 = dialogLayout.findViewById<ImageView>(R.id.destiny2)
        val card3 = dialogLayout.findViewById<ImageView>(R.id.destiny3)

        if (megamiName == "inuru") {
            if (!isFront) {
                card0.setImageResource(R.drawable.na_26_innealra_o1_f_3_az)
                card1.setImageResource(R.drawable.na_26_innealra_o1_f_4_az)
                card2.setImageResource(R.drawable.na_26_innealra_o1_f_2_az)
                card3.setImageResource(R.drawable.na_26_innealra_o1_f_1_az)
            } else {
                card0.setImageResource(R.drawable.na_26_innealra_o1_f_3)
                card1.setImageResource(R.drawable.na_26_innealra_o1_f_4)
                card2.setImageResource(R.drawable.na_26_innealra_o1_f_2)
                card3.setImageResource(R.drawable.na_26_innealra_o1_f_1)

            }
        } else if (megamiName == "mahiru") {
            if (!isFront) {
                card0.setImageResource(R.drawable.na_26_innealra_o2_f_3_az)
                card1.setImageResource(R.drawable.na_26_innealra_o2_f_4_az)
                card2.setImageResource(R.drawable.na_26_innealra_o2_f_2_az)
                card3.setImageResource(R.drawable.na_26_innealra_o2_f_1_az)
            } else {
                card0.setImageResource(R.drawable.na_26_innealra_o2_f_3)
                card1.setImageResource(R.drawable.na_26_innealra_o2_f_4)
                card2.setImageResource(R.drawable.na_26_innealra_o2_f_2)
                card3.setImageResource(R.drawable.na_26_innealra_o2_f_1)

            }
        } else if (megamiName == "akuru") {
            if (!isFront) {
                card0.setImageResource(R.drawable.na_26_innealra_o3_f_3_az)
                card1.setImageResource(R.drawable.na_26_innealra_o3_f_4_az)
                card2.setImageResource(R.drawable.na_26_innealra_o3_f_2_az)
                card3.setImageResource(R.drawable.na_26_innealra_o3_f_1_az)
            } else {
                card0.setImageResource(R.drawable.na_26_innealra_o3_f_3)
                card1.setImageResource(R.drawable.na_26_innealra_o3_f_4)
                card2.setImageResource(R.drawable.na_26_innealra_o3_f_2)
                card3.setImageResource(R.drawable.na_26_innealra_o3_f_1_s10_2)

            }
        }

        // スクロール位置を上に戻す
        val scrollView = dialogLayout.findViewById<ScrollView>(R.id.destinyCardsScrollView)
        scrollView.post {
            scrollView.fullScroll(View.FOCUS_UP)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());
        // inflate = レイアウトXMLからビューを生成するもの
        val inflater = requireActivity().layoutInflater;
        val dialogLayout = inflater.inflate(R.layout.dialog_showdestinycards, null);

        isFront = true;

        // データ取得
        megamiName = arguments?.getString("megamiName");

        // 裏面ボタン取得
        val changeSurfaceButton = dialogLayout.findViewById<Button>(R.id.changeSurface)
        changeSurfaceButton.setOnClickListener {
            isFront = !isFront
            setData(dialogLayout)
        }

        // データ設定
        setData(dialogLayout)

        builder.setView(dialogLayout);

        return builder.create();
    }
}
