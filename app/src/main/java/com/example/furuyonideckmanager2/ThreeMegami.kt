package com.example.furuyonideckmanager2

import io.realm.kotlin.types.RealmInstant
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

open class ThreeMegami: RealmObject {
    @PrimaryKey
    var id: String = ""
    var title: String = ""
    var date: RealmInstant = RealmInstant.now();
    var megami0: String = ""
    var megami1: String = ""
    var megami2: String = ""
    var comment: String = ""
    var deck01id: String = ""
    var deck12id: String = ""
    var deck20id: String = ""
    var deck01name: String = "";
    var deck12name: String = "";
    var deck20name: String = "";

    fun copy(): ThreeMegami {
        val copy = ThreeMegami()
        copy.id = this.id
        copy.title = this.title
        copy.date = this.date
        copy.megami0 = this.megami0
        copy.megami1 = this.megami1
        copy.megami2 = this.megami2
        copy.comment = this.comment
        copy.deck01id = this.deck01id
        copy.deck12id = this.deck12id
        copy.deck20id = this.deck20id
        copy.deck01name = this.deck01name
        copy.deck12name = this.deck12name
        copy.deck20name = this.deck20name
        return copy
    }
}