package com.primoz.timetimer.widgets

import android.content.Context
import android.os.Handler
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import com.primoz.timetimer.R
import com.primoz.timetimer.extras.Util
import com.primoz.timetimer.main.prepare.TotalTimeListener
import kotlinx.android.synthetic.main.layout_create_program.view.*

/**
 * Created by Primož on 21/04/2018.
 */


class CreateProgramView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ConstraintLayout(context, attrs, defStyleAttr) {
    val TO_CHANGE = 5
    val REPEAT_DELAY = 75
    private val repeatUpdateHandler = Handler()
    private var mAutoIncrement = false
    private var mAutoDecrement = false

    private lateinit var listener: TotalTimeListener

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_create_program, this, true)
        settingListenersForWork()
        settingListenersForRest()
        settingListenersForRounds()
    }

    fun setUpdateListener(listener: TotalTimeListener) {
        this.listener = listener
    }

    fun setWork(seconds: Int) {
        val s = Util.getSeconds(seconds)
        val min = Util.getMinutes(seconds)
        val timeString = String.format("%02d:%02d", min, s)
        cTimeWork.text = timeString
    }

    fun setRest(seconds: Int) {
        val s = Util.getSeconds(seconds)
        val min = Util.getMinutes(seconds)
        val timeString = String.format("%02d:%02d", min, s)
        cTimeRest.text = timeString
    }

    fun setRounds(rounds: Int) {
        tvRounds.text = rounds.toString()
    }

    fun getWorkSeconds(): Int {
        if (cTimeWork == null) return 0
        val time = cTimeWork.text.toString()
        return Util.getTotalSecondsFromString(time)
    }

    fun getRestSeconds(): Int {
        if (cTimeRest == null) return 0
        val time = cTimeRest.text.toString()
        return Util.getTotalSecondsFromString(time)
    }

    fun getRounds(): Int {
        return if (tvRounds == null) 0 else Integer.valueOf(tvRounds.text.toString())
    }

    private fun getTotalSessionSeconds(): Int {
        val work = getWorkSeconds() * getRounds()
        val rest = getRestSeconds() * getRounds()
        return if (getRounds() > 1) {
            work + rest
        } else work
    }

    /*Increasing seconds*/
    private fun increase(idTextView: Int) {
        when (idTextView) {
            R.id.cTimeWork -> {
                var totalSeconds = getWorkSeconds()
                totalSeconds += TO_CHANGE
                setWork(totalSeconds)
            }
            R.id.cTimeRest -> {
                var totalSeconds = getRestSeconds()
                totalSeconds += TO_CHANGE
                setRest(totalSeconds)
            }
            R.id.tvRounds -> {
                var rounds = getRounds()
                rounds += 1
                setRounds(rounds)
            }
        }

        val hours = Util.getHours(getTotalSessionSeconds())
        val minutes = Util.getMinutes(getTotalSessionSeconds())
        val seconds = Util.getSeconds(getTotalSessionSeconds())
        listener.updateTotalTime(hours, minutes, seconds)
    }

    /*Decreasing seconds*/
    private fun decrease(idTextView: Int) {
        when (idTextView) {
            R.id.cTimeWork -> {
                var totalSeconds = getWorkSeconds()
                if (totalSeconds > 5) {
                    totalSeconds -= TO_CHANGE
                    setWork(totalSeconds)
                }
            }
            R.id.cTimeRest -> {
                var totalSeconds = getRestSeconds()
                if (totalSeconds > 0) {
                    totalSeconds -= TO_CHANGE
                    setRest(totalSeconds)
                }
            }
            R.id.tvRounds -> {
                var rounds = getRounds()
                if (rounds > 1) {
                    rounds -= 1
                    setRounds(rounds)
                }
            }
        }
        val hours = Util.getHours(getTotalSessionSeconds())
        val minutes = Util.getMinutes(getTotalSessionSeconds())
        val seconds = Util.getSeconds(getTotalSessionSeconds())
        listener.updateTotalTime(hours, minutes, seconds)
    }


    private fun settingListenersForWork() {
        tvRemoveTimeTrain.setOnClickListener {
            if (cTimeWork.text != null) {
                decrease(cTimeWork.id)
            }
        }

        tvRemoveTimeTrain.setOnLongClickListener {
            mAutoDecrement = true
            repeatUpdateHandler.post(RepeatUpdaterTrain())
            false
        }

        tvRemoveTimeTrain.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoDecrement) {
                mAutoDecrement = false
            }
            false
        }


        tvAddTimeTrain.setOnClickListener { increase(cTimeWork.id) }

        tvAddTimeTrain.setOnLongClickListener {
            mAutoIncrement = true
            repeatUpdateHandler.post(RepeatUpdaterTrain())
            false
        }

        tvAddTimeTrain.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoIncrement) {
                mAutoIncrement = false
            }
            false
        }
    }


    private fun settingListenersForRest() {
        tvRemoveTimeRest.setOnClickListener {
            if (cTimeRest.text != null) {
                decrease(cTimeRest.id)
            }
        }

        tvRemoveTimeRest.setOnLongClickListener {
            mAutoDecrement = true
            repeatUpdateHandler.post(RepeatUpdaterRest())
            false
        }

        tvRemoveTimeRest.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoDecrement) {
                mAutoDecrement = false
            }
            false
        }


        tvAddTimeRest.setOnClickListener { increase(cTimeRest.id) }

        tvAddTimeRest.setOnLongClickListener {
            mAutoIncrement = true
            repeatUpdateHandler.post(RepeatUpdaterRest())
            false
        }

        tvAddTimeRest.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoIncrement) {
                mAutoIncrement = false
            }
            false
        }
    }


    private fun settingListenersForRounds() {
        tvRemoveRound.setOnClickListener {
            if (tvRounds.text != null) decrease(tvRounds.id)
        }

        tvRemoveRound.setOnLongClickListener {
            mAutoDecrement = true
            repeatUpdateHandler.post(RepeatUpdaterRounds())
            false
        }

        tvRemoveRound.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoDecrement) {
                mAutoDecrement = false
            }
            false
        }

        tvAddRound.setOnClickListener { increase(tvRounds.id) }

        tvAddRound.setOnLongClickListener {
            mAutoIncrement = true
            repeatUpdateHandler.post(RepeatUpdaterRounds())
            false
        }
        tvAddRound.setOnTouchListener { v, event ->
            if ((event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) && mAutoIncrement) {
                mAutoIncrement = false
            }
            false
        }
    }


    /*We use RepeatUpdater class for auto-incrementing on button hold*/
    private inner class RepeatUpdaterTrain : Runnable {
        override fun run() {
            if (mAutoIncrement) {
                increase(cTimeWork.id)
                repeatUpdateHandler.postDelayed(RepeatUpdaterTrain(), REPEAT_DELAY.toLong())
            } else if (mAutoDecrement) {
                decrease(cTimeWork.id)
                repeatUpdateHandler.postDelayed(RepeatUpdaterTrain(), REPEAT_DELAY.toLong())
            }
        }
    }


    /*We use RepeatUpdater class for auto-incrementing on button hold*/
    private inner class RepeatUpdaterRest : Runnable {
        override fun run() {
            if (mAutoIncrement) {
                increase(cTimeRest.id)
                repeatUpdateHandler.postDelayed(RepeatUpdaterRest(), REPEAT_DELAY.toLong())
            } else if (mAutoDecrement) {
                decrease(cTimeRest.id)
                repeatUpdateHandler.postDelayed(RepeatUpdaterRest(), REPEAT_DELAY.toLong())
            }
        }
    }


    /*We use RepeatUpdater class for auto-incrementing on button hold*/
    private inner class RepeatUpdaterRounds : Runnable {
        override fun run() {
            if (mAutoIncrement) {
                increase(tvRounds.id)
                repeatUpdateHandler.postDelayed(RepeatUpdaterRounds(), REPEAT_DELAY.toLong())
            } else if (mAutoDecrement) {
                decrease(tvRounds.id)
                repeatUpdateHandler.postDelayed(RepeatUpdaterRounds(), REPEAT_DELAY.toLong())
            }
        }
    }

}