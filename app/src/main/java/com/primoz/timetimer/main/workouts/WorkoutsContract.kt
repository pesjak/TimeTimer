package com.primoz.timetimer.main.workouts

import com.primoz.timetimer.BasePresenter
import com.primoz.timetimer.BaseView
import com.primoz.timetimer.data_mvp.Workout

import io.realm.RealmList

/**
 * Created by Primož Pesjak on 22/09/2017.
 */

interface WorkoutsContract {

    interface View : BaseView<Presenter> {
        fun showWorkouts(workouts: RealmList<Workout>)

        fun showNoWorkouts(workouts: RealmList<Workout>)

        fun showAddWorkout()

        fun showWorkoutDetailUI(workoutID: Int)

        fun showStartWorkout(workoutID: Int)

        fun showSuccessfullySavedMessage()

        fun showSuccessfullyDeletedMessage()

    }

    interface Presenter : BasePresenter {
        fun result(requestCode: Int, resultCode: Int)

        fun loadWorkouts()

        fun addNewWorkout()

        fun editWorkout(workoutID: Int)

        fun removeWorkout(workoutID: Int)

        fun startWorkout(workoutID: Int)

    }
}
