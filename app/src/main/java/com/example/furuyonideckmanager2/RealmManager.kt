package com.example.furuyonideckmanager2

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.migration.AutomaticSchemaMigration

object RealmManager{
    private var realm: Realm? = null

    val config = RealmConfiguration.Builder(
        schema = setOf(
            ThreeMegami::class,
            Deck::class
        )
    )
        .schemaVersion(1)
        .migration(
            AutomaticSchemaMigration { migration ->
                val oldVersion = migration.oldRealm.schemaVersion()

                if (oldVersion < 1) {
                    // ThreeMegami はモデルクラスとして定義済みなので、
                    // スキーマの自動追加は SDK が処理してくれます。
                    // データ移行などが必要ならここで行います。
                }
            }
        )
        .build()

    fun getRealm(): Realm {
        if (realm == null || realm?.isClosed() == true) {
            realm = Realm.open(config)
        }
        return realm!!
    }

    fun closeRealm() {
        realm?.close()
        realm = null
    }
}