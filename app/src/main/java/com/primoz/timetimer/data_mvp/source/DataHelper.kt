package com.primoz.timetimer.data_mvp.source

import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutPOJO

import io.realm.Realm
import io.realm.kotlin.where

/**
 * Created by Primož on 21/08/2017.
 */

object DataHelper {

    fun addNewWorkout(realm: Realm, workoutPOJO: WorkoutPOJO) {
        realm.executeTransaction { realmInstance ->
            val name = workoutPOJO.name
            val timeTotal = workoutPOJO.timeTotal
            val timeWork = workoutPOJO.timeWork
            val timeRest = workoutPOJO.timeRest
            val timeRounds = workoutPOJO.timeRounds

            Workout.createWorkout(realmInstance, name, timeTotal, timeWork, timeRest, timeRounds)
        }
    }


    fun editWorkout(realm: Realm, workoutID: Long, workoutPOJO: WorkoutPOJO) {
        realm.executeTransaction { realmInstance ->
            val workout = realmInstance.where<Workout>().equalTo("workoutID", workoutID).findFirst()

            val name = workoutPOJO.name
            val timeTotal = workoutPOJO.timeTotal
            val timeWork = workoutPOJO.timeWork
            val timeRest = workoutPOJO.timeRest
            val timeRounds = workoutPOJO.timeRounds

            workout!!.name = name
            workout.timeTotal = timeTotal
            workout.timeWork = timeWork
            workout.timeRest = timeRest
            workout.timeRounds = timeRounds
        }
    }

    fun deleteItemAsync(realm: Realm, workoutID: Long) {
        realm.executeTransaction { realmInstance -> Workout.delete(realmInstance, workoutID) }
    }

}
