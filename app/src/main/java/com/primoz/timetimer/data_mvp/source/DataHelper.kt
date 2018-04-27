package com.primoz.timetimer.data_mvp.source

import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutPOJO
import com.primoz.timetimer.data_mvp.WorkoutsDataSource

import io.realm.Realm
import io.realm.kotlin.where

/**
 * Created by Primož on 21/08/2017.
 */

object DataHelper {

    fun addNewWorkout(realm: Realm, workoutPOJO: WorkoutPOJO, listener: WorkoutsDataSource.SaveWorkoutCallback? = null) {
        realm.executeTransaction { realmInstance ->
            val name = workoutPOJO.name
            val timeWork = workoutPOJO.timeWork
            val timeRest = workoutPOJO.timeRest
            val timeRounds = workoutPOJO.timeRounds

            Workout.createWorkout(realmInstance, name, timeWork, timeRest, timeRounds)
            listener?.onWorkoutSaved()
        }
    }


    fun editWorkout(realm: Realm, workoutID: Int, workoutPOJO: WorkoutPOJO, listener: WorkoutsDataSource.EditWorkoutCallback? = null) {
        realm.executeTransaction { realmInstance ->
            realmInstance.where<Workout>().equalTo("workoutID", workoutID).findFirst()?.let {
                it.name = workoutPOJO.name
                it.timeWork = workoutPOJO.timeWork
                it.timeRest = workoutPOJO.timeRest
                it.timeRounds = workoutPOJO.timeRounds
                listener?.onWorkoutEdited()
            }
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
