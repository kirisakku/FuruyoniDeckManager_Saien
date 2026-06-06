package com.example.furuyonideckmanager2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.appcompat.widget.AppCompatEditText
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.DialogFragment


class CommentDialog: DialogFragment() {
    interface Listener {
        fun update();
        fun cancel();
    }
    private var listener: Listener? = null;
    private var text: String = "";

    override fun onAttach(context: Context) {
        super.onAttach(context);
        when (context) {
            is Listener -> listener = context;
        }
    }

    fun getInput(): String? {
        return text;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());
        // inflate = レイアウトXMLからビューを生成するもの
        val inflater = requireActivity().layoutInflater;
        val dialogLayout = inflater.inflate(R.layout.dialog_comment, null);

        builder.setView(dialogLayout)
            .setPositiveButton(R.string.update) { dialog, which ->
                listener?.update();
            }
            .setNegativeButton(R.string.cancel) {dialog, which ->
                // 画面閉じる処理
                getDialog()?.cancel();
            }

        builder.apply {
            val deckComment = dialogLayout.findViewById<AppCompatEditText>(R.id.deckComment);
            val currentComment = arguments?.getString("comment", "");
            deckComment.setText(currentComment);
        }

        val dialog = builder.create();

        // バリデーション設定
        val editText = dialogLayout.findViewById<AppCompatEditText>(R.id.deckComment);
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // テキスト情報を更新
                text = s.toString();
            }
        })

        return dialog;
    }
}