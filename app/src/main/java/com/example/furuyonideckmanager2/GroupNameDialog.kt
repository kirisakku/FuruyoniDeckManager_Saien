package com.example.furuyonideckmanager2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.fragment.app.DialogFragment

class GroupNameDialog: DialogFragment() {
    interface Listener {
        fun confirm();
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

    fun getInput(): String {
        return text;
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity());
        // inflate = レイアウトXMLからビューを生成するもの
        val inflater = requireActivity().layoutInflater;
        val dialogLayout = inflater.inflate(R.layout.dialog_groupname, null);
        val deckComment = dialogLayout.findViewById<EditText>(R.id.deckComment);

        builder.setView(dialogLayout)
            .setPositiveButton(R.string.register) { _, _ ->
                val text = deckComment.text.toString();
                listener?.confirm();
            }
            .setNegativeButton(R.string.cancel) {_, _ ->
                // 画面閉じる処理
                getDialog()?.cancel();
            }

        val dialog = builder.create();
        // デフォルト値設定
        val defaultName = arguments?.getString("defaultName");
        deckComment.setText(defaultName);
        text = if (defaultName != null) defaultName else "";

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        };

        // バリデーション設定
        deckComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                text = s.toString();
                val registerButton = dialog.getButton(BUTTON_POSITIVE);
                if (s.length == 0) {
                    deckComment.setError("必須項目です。グループ名を入力してください");
                    registerButton.setEnabled(false);
                } else if (s.length > 20) {
                    deckComment.setError("グループ名は20文字以内で入力してください");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        })

        return dialog;
    }
}