package DBUtil;

import com.example.furuyonideckmanager2.ThreeMegami
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query

/**
 * 選択されたデッキ情報を登録。
 * @param realm realm
 * @param id 三柱管理ID
 * @param fileName デッキのUUID
 * @param title デッキタイトル
 * @param target 対象のペア。01、12、20のいずれか。
 */
suspend fun registerSelectedDeck(realm: Realm, id: String, fileName: String, title: String, target: String) {
    val targetData = realm.query<ThreeMegami>("id == $0", id).first().find()
    if (targetData != null) {
        realm.write {
            val obj = findLatest(targetData)
            if (obj != null) {
                when (target) {
                    "01" -> {
                        obj.deck01id = fileName
                        obj.deck01name = title
                    }
                    "12" -> {
                        obj.deck12id = fileName
                        obj.deck12name = title
                    }
                    "20" -> {
                        obj.deck20id = fileName
                        obj.deck20name = title
                    }
                }
            }
        }
    }
}