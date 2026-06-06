package com.example.furuyonideckmanager2

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface.BUTTON_POSITIVE
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class DeckNameDialog: DialogFragment() {
    interface Listener {
        suspend fun register();
        fun cancel();
    }
    private var listener: Listener? = null;
    private var text: String? = null;

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
        val dialogLayout = inflater.inflate(R.layout.dialog_register, null);
        builder.setView(dialogLayout)
            .setPositiveButton(R.string.register) { _, _ ->
                lifecycleScope.launch {
                    listener?.register();
                }
            }
            .setNegativeButton(R.string.cancel) {_, _ ->
                // 画面閉じる処理
                getDialog()?.cancel();
            }

        val dialog = builder.create();

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        };

        // バリデーション設定
        val editText = dialogLayout.findViewById<AppCompatEditText>(R.id.deckComment);
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                text = s.toString();
                val registerButton = dialog.getButton(BUTTON_POSITIVE);
                if (s.length == 0) {
                    editText.setError("必須項目です。デッキ名を入力してください");
                    registerButton.setEnabled(false);
                } else if (s.length > 15) {
                    editText.setError("デッキ名は15文字以内で入力してください");
                    registerButton.setEnabled(false);
                } else {
                    registerButton.setEnabled(true);
                }
            }
        })

        return dialog;
    }
}