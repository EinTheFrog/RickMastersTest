package com.example.rickmasterstest.model.cache

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class CameraCache: RealmObject() {
    @PrimaryKey var id: Int = 0
    @Required var name: String = ""
    @Required var snapshot: String = ""
    @Required var room: String = ""
    var favorites: Boolean = false
    var rec: Boolean = false
}