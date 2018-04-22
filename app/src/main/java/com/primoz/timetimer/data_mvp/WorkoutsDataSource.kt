package com.primoz.timetimer.data_mvp

import io.realm.RealmList

/**
 * Created by Primož Pesjak on 22/09/2017.
 */

interface WorkoutsDataSource {

    interface LoadWorkoutsCallback {
        fun onWorkoutsLoaded(workouts: RealmList<Workout>)

        fun onDataNotAvailable(workouts: RealmList<Workout>)

    }

    interface GetWorkoutCallback {
        fun onWorkoutLoaded(workout: Workout)

        fun onDataNotAvailable()
    }


    interface SavedWorkoutCallback {
        fun onWorkoutSaved()
    }

    interface EditedWorkoutCallback {
        fun onWorkoutEdited()
    }

    interface DeletedWorkoutCallback {
        fun onWorkoutDeleted()
    }

    fun getWorkouts(callback: LoadWorkoutsCallback)

    fun getWorkout(workoutID: Long, callback: GetWorkoutCallback)

    fun saveWorkout(workout: WorkoutPOJO, callback: SavedWorkoutCallback)

    fun editWorkout(workoutID: Long, workout: WorkoutPOJO, callback: EditedWorkoutCallback)

    fun deleteWorkout(workoutID: Long, callback: DeletedWorkoutCallback)
}

