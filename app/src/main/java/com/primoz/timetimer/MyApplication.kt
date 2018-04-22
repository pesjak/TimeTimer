package com.primoz.timetimer

import android.app.Application
import com.primoz.timetimer.data_mvp.WorkoutList

import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.kotlin.createObject

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .initialData { realm -> realm.createObject<WorkoutList>() }
                .build()

        Realm.deleteRealm(realmConfig) // Delete Realm between app restarts.
        Realm.setDefaultConfiguration(realmConfig)
    }
}