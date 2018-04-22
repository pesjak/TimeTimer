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
            val timeWork = workoutPOJO.timeWork
            val timeRest = workoutPOJO.timeRest
            val timeRounds = workoutPOJO.timeRounds

            Workout.createWorkout(realmInstance, name, timeWork, timeRest, timeRounds)
        }
    }


    fun editWorkout(realm: Realm, workoutID: Int, workoutPOJO: WorkoutPOJO) {
        realm.executeTransaction { realmInstance ->
            val workout = realmInstance.where<Workout>().equalTo("workoutID", workoutID).findFirst()

            val name = workoutPOJO.name
            val timeWork = workoutPOJO.timeWork
            val timeRest = workoutPOJO.timeRest
            val timeRounds = workoutPOJO.timeRounds

            workout!!.name = name
            workout.timeWork = timeWork
            workout.timeRest = timeRest
            workout.timeRounds = timeRounds
        }
    }

    fun deleteItemAsync(realm: Realm, workoutID: Int) {
        realm.executeTransaction { realmInstance -> Workout.delete(realmInstance, workoutID) }
    }

    fun addOrEditWorkout(realm: Realm, title: String, work: Int, rest: Int, rounds: Int) {
        val workout = realm.where<Workout>().equalTo("name", title).findFirst()
        if (workout != null) { //Edit
            DataHelper.editWorkout(realm, workout.workoutID,
                    WorkoutPOJO(title,
                            work,
                            rest,
                            rounds))
        } else {//New
            DataHelper.addNewWorkout(realm, WorkoutPOJO(title,
                    work,
                    rest,
                    rounds))
        }
    }

}
