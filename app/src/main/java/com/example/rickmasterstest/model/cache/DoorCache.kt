package com.example.rickmasterstest.model.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class DoorCache: RealmObject() {
    @PrimaryKey var id: Int = 0
    @Required var name: String = ""
    var snapshot: String? = null
    var favorites: Boolean = false
}