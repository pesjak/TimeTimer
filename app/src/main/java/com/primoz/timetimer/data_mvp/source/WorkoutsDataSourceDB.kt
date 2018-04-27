package com.primoz.timetimer.data_mvp.source

import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutList
import com.primoz.timetimer.data_mvp.WorkoutPOJO
import com.primoz.timetimer.data_mvp.WorkoutsDataSource

import io.realm.Realm
import io.realm.kotlin.where

class WorkoutsDataSourceDB : WorkoutsDataSource {

    override fun getWorkouts(callback: WorkoutsDataSource.LoadWorkoutsCallback) {
        val realm = Realm.getDefaultInstance()

        val workoutRealmResults = realm.where<WorkoutList>().findFirst()!!.workoutRealmList

        if (workoutRealmResults != null) {
            if (workoutRealmResults.isEmpty()) {
                callback.onDataNotAvailable(workoutRealmResults)
            } else {
                callback.onWorkoutsLoaded(workoutRealmResults)
            }
        }
    }

    override fun getWorkout(workoutID: Int, callback: WorkoutsDataSource.GetWorkoutCallback) {
        val realm = Realm.getDefaultInstance()
        val workout =  realm.where<Workout>().equalTo("workoutID", workoutID).findFirst()
        if (workout == null) {
            callback.onDataNotAvailable()
        } else {
            callback.onWorkoutLoaded(workout)
        }
    }

    override fun saveWorkout(workout: WorkoutPOJO, callback: WorkoutsDataSource.SaveWorkoutCallback) {
        val realm = Realm.getDefaultInstance()
        DataHelper.addNewWorkout(realm, workout)
        callback.onWorkoutSaved()
    }

    override fun editWorkout(workoutID: Int, workout: WorkoutPOJO, callback: WorkoutsDataSource.EditWorkoutCallback) {
        val realm = Realm.getDefaultInstance()
        DataHelper.editWorkout(realm, workoutID, workout)
        callback.onWorkoutEdited()
    }

    override fun deleteWorkout(workoutID: Int, callback: WorkoutsDataSource.DeleteWorkoutCallback) {
        val realm = Realm.getDefaultInstance()
        DataHelper.deleteItemAsync(realm, workoutID)
        callback.onWorkoutDeleted()
    }

    companion object {
        var INSTANCE: WorkoutsDataSourceDB? = null
        fun getInstance(): WorkoutsDataSourceDB {
            if (INSTANCE == null) {
                INSTANCE = WorkoutsDataSourceDB()
            }
            return INSTANCE as WorkoutsDataSourceDB
        }
    }
}
