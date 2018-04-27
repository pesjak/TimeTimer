package com.primoz.timetimer.data_mvp

import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.kotlin.createObject
import io.realm.kotlin.where

open class Workout : RealmObject() {

    @PrimaryKey
    var workoutID: Int = 0
    var name: String = ""
    var timeWork: Int = 0
    var timeRest: Int = 0
    var timeRounds: Int = 0
    val totalSessionSeconds: Int
        get() {
            val trainingTogether = timeWork * timeRounds
            val restTogether = timeRest * timeRounds
            return trainingTogether + restTogether
        }

    companion object {

        fun delete(realm: Realm, workoutID: Int) {
            val workout = realm.where<Workout>().equalTo("workoutID", workoutID).findFirst()
            // Otherwise it has been deleted already.
            workout?.deleteFromRealm()
        }

        fun createWorkout(realm: Realm, name: String, timeWork: Int, timeRest: Int, timeRounds: Int) {
            val list = realm.where<WorkoutList>().findFirst()
            val workouts = list!!.workoutRealmList

            var nextID = realm.where<Workout>().max("workoutID")
            nextID = if (nextID == null) 1 else nextID.toLong() + 1 //Gets MAX KEY_ID_PROGRAM +1 to be set

            val workout = realm.createObject<Workout>(nextID)
            workout.name = name
            workout.timeWork = timeWork
            workout.timeRest = timeRest
            workout.timeRounds = timeRounds
            workouts!!.add(workout)
        }

        fun exists(realm: Realm, workoutName: String): Boolean {
            val workout = realm.where<Workout>().equalTo("name", workoutName).findFirst()
            if(workout != null) return true
            return false
        }
    }
}
