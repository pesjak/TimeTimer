package com.primoz.timetimer.data_mvp

import io.realm.RealmList

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

    override fun getWorkout(workoutID: Int, callback: WorkoutsDataSource.GetWorkoutCallback) {
        mWorkoutsDataSource.getWorkout(workoutID, callback)
    }

    override fun saveWorkout(workout: WorkoutPOJO, callback: WorkoutsDataSource.SaveWorkoutCallback) {
        mWorkoutsDataSource.saveWorkout(workout, callback)
    }

    override fun editWorkout(workoutID: Int, workout: WorkoutPOJO, callback: WorkoutsDataSource.EditWorkoutCallback) {
        mWorkoutsDataSource.editWorkout(workoutID, workout, callback)
    }

    override fun deleteWorkout(workoutID: Int, callback: WorkoutsDataSource.DeleteWorkoutCallback) {
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
