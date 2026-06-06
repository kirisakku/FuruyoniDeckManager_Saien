package com.example.furuyonideckmanager2

import android.content.res.Resources
import android.widget.Toast
import java.io.*

fun readFile(thisArg: RandomChooseActivity): List<List<String>> {
    val res: Resources = thisArg.getResources();
    var bufferReader: BufferedReader? = null;
    var separatedList = mutableListOf<List<String>>();
    try {
        try {
            val inputStream = res.openRawResource(R.raw.megamilist);
            bufferReader = BufferedReader(InputStreamReader(inputStream));
            var str = bufferReader.readLine();
            while(str != null) {
                separatedList.add(str.split(','))
                str = bufferReader.readLine();
            }
        } finally {
            if (bufferReader != null) {
                bufferReader.close();
            }
        }
    } catch (e: IOException) {
        Toast.makeText(thisArg, "読み込み失敗", Toast.LENGTH_SHORT).show();
    }

    return separatedList;
}

fun getList(thisArg: RandomChooseActivity, checkedMegami: MutableList<String>): MutableList<Map<String, String>> {
    val megamiList = readFile(thisArg);
    var objectList = mutableListOf<Map<String, String>>();
    megamiList.forEach{
        val megamiName = it[0];
        val megamiCategory = it[1];
        val image = it[2];
        val map: Map<String, String> =
            mapOf("megamiName" to megamiName, "megamiCategory" to megamiCategory, "image" to image);

        // 選択候補であれば追加
        if (checkedMegami.any{megamiName == it}) {
            objectList.add(map);
        }
    }

    return objectList;
}