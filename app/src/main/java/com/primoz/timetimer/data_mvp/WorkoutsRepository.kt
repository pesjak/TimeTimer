package com.primoz.timetimer.data_mvp

import io.realm.RealmList


/**
 * Created by Primož Pesjak on 22/09/2017.
 */

class WorkoutsRepository(private val mWorkoutsDataSource: WorkoutsDataSource) : WorkoutsDataSource {

    fun destroyInstance() {
        INSTANCE = null
    }


    override fun getWorkouts(callback: WorkoutsDataSource.LoadWorkoutsCallback) {
        mWorkoutsDataSource.getWorkouts(object : WorkoutsDataSource.LoadWorkoutsCallback {
            override fun onWorkoutsLoaded(workouts: RealmList<Workout>) {
                callback.onWorkoutsLoaded(workouts)

            }

            override fun onDataNotAvailable(workouts: RealmList<Workout>) {
                callback.onDataNotAvailable(workouts)
            }
        })
    }

    override fun getWorkout(workoutID: Long, callback: WorkoutsDataSource.GetWorkoutCallback) {
        mWorkoutsDataSource.getWorkout(workoutID, callback)
    }

    override fun saveWorkout(workout: WorkoutPOJO, callback: WorkoutsDataSource.SavedWorkoutCallback) {
        mWorkoutsDataSource.saveWorkout(workout, callback)
    }

    override fun editWorkout(workoutID: Long, workout: WorkoutPOJO, callback: WorkoutsDataSource.EditedWorkoutCallback) {
        mWorkoutsDataSource.editWorkout(workoutID, workout, callback)
    }

    override fun deleteWorkout(workoutID: Long, callback: WorkoutsDataSource.DeletedWorkoutCallback) {
        mWorkoutsDataSource.deleteWorkout(workoutID, callback)
    }

    companion object {

        private var INSTANCE: WorkoutsRepository? = null
        fun getInstance(mWorkoutsDataSource: WorkoutsDataSource): WorkoutsRepository {
            if (INSTANCE == null) {
                INSTANCE = WorkoutsRepository(mWorkoutsDataSource)
            }
            return INSTANCE as WorkoutsRepository
        }
    }

}
