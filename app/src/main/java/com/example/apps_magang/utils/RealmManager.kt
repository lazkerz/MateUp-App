package com.example.apps_magang.utils

import io.realm.Realm
import io.realm.RealmConfiguration

object RealmManager {
    private var realm: Realm? = null

    fun initRealm() {
        val realmConfig = RealmConfiguration.Builder()
            .name("apps.realm")
            .deleteRealmIfMigrationNeeded()
            .schemaVersion(20)
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(realmConfig)
    }

    fun openRealm(): Realm {
        if (realm == null || realm?.isClosed == true) {
            realm = Realm.getDefaultInstance()
        }
        return realm!!
    }

    fun closeRealm() {
        realm?.close()
        realm = null
    }
}
