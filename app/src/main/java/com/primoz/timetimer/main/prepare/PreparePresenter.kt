package com.primoz.timetimer.main.prepare

import com.primoz.timetimer.data_mvp.WorkoutsRepository
import com.primoz.timetimer.main.workouts.WorkoutsContract

/**
 * Created by Primož on 21/04/2018.
 */

class PreparePresenter(private var mWorkoutsRepository: WorkoutsRepository, var mView: PrepareContract.View) : PrepareContract.Presenter {
    init {
        mView.setPresenter(this)
    }
    override fun start() {

    }

    override fun loadTitle() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadWork() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadRest() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadRounds() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    override fun saveProgram(title: String, work: Int, rest: Int, rounds: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun playProgram(id: Int) {
        mView.showPlayFragment(id)
    }

}