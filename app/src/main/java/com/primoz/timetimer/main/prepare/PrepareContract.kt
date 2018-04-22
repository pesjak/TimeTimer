package com.primoz.timetimer.main.prepare

import android.content.Context
import com.primoz.timetimer.BasePresenter
import com.primoz.timetimer.BaseView

/**
 * Created by Primož Pesjak on 22/09/2017.
 */

interface PrepareContract {

    interface View : BaseView<Presenter> {
        fun showTitle(title: String)
        fun showWork(work: Int)
        fun showRest(rest: Int)
        fun showRounds(rounds: Int)
        fun showTotalTime(seconds: String)
        fun showDialogAbort()
        fun showPlayFragment(idOfProgram: Int)
        fun showEnterTitle()
        fun closePrepare()
    }

    interface Presenter : BasePresenter {
        fun loadTitle()
        fun loadWork()
        fun loadRest()
        fun loadRounds()
        fun loadTotalTime(hours: Int, minutes: Int, seconds: Int)

        fun playProgram(id: Int)
        fun saveProgram(title: String, work: Int, rest: Int, rounds: Int)
        fun loadProgram(idOfProgram: Int)
    }
}
