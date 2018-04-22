package com.primoz.timetimer.data_mvp

/**
 * Created by Primož on 21/08/2017.
 */

open class WorkoutPOJO(var name: String, var timeTotal: Int, var timeWork: Int, var timeRest: Int, var timeRounds: Int) {
    val totalSessionSeconds: Int
        get() {
            val trainingTogether = timeWork * timeRounds
            val restTogether = timeRest * timeRounds
            return trainingTogether + restTogether
        }
}
