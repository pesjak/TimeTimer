package com.primoz.timetimer.data_mvp

import io.realm.RealmList
import io.realm.RealmObject

/**
 * Created by Primož on 21/08/2017.
 */

open class WorkoutList : RealmObject() {
    var workoutRealmList: RealmList<Workout>? = null
}
