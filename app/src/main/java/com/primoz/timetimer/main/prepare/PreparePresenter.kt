package com.primoz.timetimer.main.prepare

import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutPOJO
import com.primoz.timetimer.data_mvp.WorkoutsDataSource
import com.primoz.timetimer.data_mvp.WorkoutsRepository
import com.primoz.timetimer.data_mvp.source.DataHelper
import io.realm.Realm

/**
 * Created by Primož on 21/04/2018. TODO please make it readable...
 */

class PreparePresenter(private var mWorkoutsRepository: WorkoutsRepository, var mView: PrepareContract.View) : PrepareContract.Presenter {
    var currentWorkoutProgram: Workout? = null

    init {
        mView.setPresenter(this)
    }

    override fun start() {

    }

    override fun loadTotalTime(hours: Int, minutes: Int, seconds: Int) {
        val timeString: String = if (hours <= 0) {
            String.format("%02d:%02d", minutes, seconds)
        } else {
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        mView.showTotalTime(timeString)
    }

    override fun loadProgram(idOfProgram: Int) {
        mWorkoutsRepository.getWorkout(idOfProgram, object : WorkoutsDataSource.GetWorkoutCallback {
            override fun onWorkoutLoaded(workout: Workout) {
                currentWorkoutProgram = workout

                loadTitle()
                loadWork()
                loadRest()
                loadRounds()
            }

            override fun onDataNotAvailable() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
    }

    override fun saveProgram(title: String, work: Int, rest: Int, rounds: Int) {
        if (title.isEmpty()) {
            mView.showEnterTitle()
            return
        }

        if (currentWorkoutProgram != null) {
            DataHelper.editWorkout(Realm.getDefaultInstance(), currentWorkoutProgram!!.workoutID, WorkoutPOJO(title,
                    work,
                    rest,
                    rounds), object : WorkoutsDataSource.EditWorkoutCallback {
                override fun onWorkoutEdited() {
                    mView.showSuccesfullyEditedWorkout()
                    mView.closePrepare()
                }
            })
        } else {
            DataHelper.addNewWorkout(Realm.getDefaultInstance(), WorkoutPOJO(title,
                    work,
                    rest,
                    rounds), object : WorkoutsDataSource.SaveWorkoutCallback {
                override fun onWorkoutSaved() {
                    mView.showSuccesfullyAddedNewWorkout()
                    mView.closePrepare()
                }
            })
        }
    }

    override fun playProgram(id: Int) {
        mView.showPlayFragment(id)
    }

    private fun loadTitle() {
        currentWorkoutProgram ?: return

        mView.showTitle(currentWorkoutProgram!!.name)
    }

    private fun loadWork() {
        currentWorkoutProgram ?: return
        mView.showWork(currentWorkoutProgram!!.timeWork)
    }

    private fun loadRest() {
        currentWorkoutProgram ?: return
        mView.showRest(currentWorkoutProgram!!.timeRest)

    }

    private fun loadRounds() {
        currentWorkoutProgram ?: return
        mView.showRounds(currentWorkoutProgram!!.timeRounds)
    }


}