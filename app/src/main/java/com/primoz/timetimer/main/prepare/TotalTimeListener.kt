package com.primoz.timetimer.main.prepare

/**
 * Created by Primož on 21/04/2018.
 */
interface TotalTimeListener {
    fun updateTotalTime(hours: Int, minutes: Int, seconds: Int)
}