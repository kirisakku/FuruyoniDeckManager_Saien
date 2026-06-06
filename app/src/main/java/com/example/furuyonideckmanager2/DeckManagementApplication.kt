package com.example.furuyonideckmanager2

import android.app.Application
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class DeckManagementApplication: Application() {
    override fun onCreate() {
        super.onCreate();

        // Realm Kotlin SDK 用の設定
        val config = RealmConfiguration.Builder(
            schema = setOf(
                Deck::class,  // ← モデルクラスを列挙
                ThreeMegami::class
                // 他にも使う RealmObject を追加
            )
        )
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded() // AutoMigrationができない場合にデバッグ用途で有効に
            .build()

        // Realm インスタンスをアプリ全体で使えるように初期化
        Realm.open(config)
    }
}