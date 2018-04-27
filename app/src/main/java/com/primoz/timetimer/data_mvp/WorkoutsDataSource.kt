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


    interface SaveWorkoutCallback {
        fun onWorkoutSaved()
    }

    interface EditWorkoutCallback {
        fun onWorkoutEdited()
    }

    interface DeleteWorkoutCallback {
        fun onWorkoutDeleted()
    }

    fun getWorkouts(callback: LoadWorkoutsCallback)

    fun getWorkout(workoutID: Int, callback: GetWorkoutCallback)

    fun saveWorkout(workout: WorkoutPOJO, callback: SaveWorkoutCallback)

    fun editWorkout(workoutID: Int, workout: WorkoutPOJO, callback: EditWorkoutCallback)

    fun deleteWorkout(workoutID: Int, callback: DeleteWorkoutCallback)
}

