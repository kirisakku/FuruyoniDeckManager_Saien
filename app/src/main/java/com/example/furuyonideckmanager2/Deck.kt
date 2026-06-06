package com.example.furuyonideckmanager2

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey


open class Deck: RealmObject {
    @PrimaryKey
    var fileName: String = ""
    var title: String = ""
    var date: RealmInstant = RealmInstant.now();
    var megami0: String = ""
    var megami1: String = ""
    var comment: String = ""

    // copyメソッドを手動で実装
    fun copy(
        fileName: String = this.fileName,
        title: String = this.title,
        date: RealmInstant = this.date,
        megami0: String = this.megami0,
        megami1: String = this.megami1
    ): Deck {
        val newDeck = Deck()
        newDeck.fileName = fileName
        newDeck.title = title
        newDeck.date = date
        newDeck.megami0 = megami0
        newDeck.megami1 = megami1
        return newDeck
    }
}