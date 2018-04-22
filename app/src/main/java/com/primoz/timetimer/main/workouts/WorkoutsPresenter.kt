package com.primoz.timetimer.main.workouts

import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutsDataSource
import com.primoz.timetimer.data_mvp.WorkoutsRepository

import io.realm.RealmList

/**
 * Created by Primož Pesjak on 22/09/2017.
 */

class WorkoutsPresenter(private var mWorkoutsRepository: WorkoutsRepository, var mWorkoutsView: WorkoutsContract.View) : WorkoutsContract.Presenter {

    init {
        mWorkoutsView.setPresenter(this)
    }

    override fun start() {
        loadWorkouts()
    }

    override fun result(requestCode: Int, resultCode: Int) {
        //Nothing for now
    }

    override fun loadWorkouts() {
        mWorkoutsRepository.getWorkouts(object : WorkoutsDataSource.LoadWorkoutsCallback {
            override fun onWorkoutsLoaded(workouts: RealmList<Workout>) {
                mWorkoutsView.showWorkouts(workouts)
            }

            override fun onDataNotAvailable(workouts: RealmList<Workout>) {
                mWorkoutsView.showNoWorkouts(workouts)
            }
        })
    }

    override fun addNewWorkout() {
        mWorkoutsView.showAddWorkout()
    }

    override fun editWorkout(workoutID: Long) {
        mWorkoutsView.showWorkoutDetailUI(workoutID)
    }

    override fun startWorkout(workoutID: Long) {
        mWorkoutsView.showStartWorkout(workoutID)
    }

    override fun removeWorkout(workoutID: Long) {
        mWorkoutsRepository.deleteWorkout(workoutID, object : WorkoutsDataSource.DeletedWorkoutCallback {
            override fun onWorkoutDeleted() {
                mWorkoutsView.showSuccessfullyDeletedMessage()
            }
        })
    }


}
