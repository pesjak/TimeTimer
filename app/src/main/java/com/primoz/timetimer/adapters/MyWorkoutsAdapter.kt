package com.primoz.timetimer.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.primoz.timetimer.R
import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.interfaces.ViewHolderWorkoutListener
import com.primoz.timetimer.viewholders.ViewHolderWorkout
import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter

/**
 * Created by Primož on 21/08/2017.
 */

class MyWorkoutsAdapter(data: OrderedRealmCollection<Workout>, private val viewHolderWorkoutListener: ViewHolderWorkoutListener) : RealmRecyclerViewAdapter<Workout, ViewHolderWorkout>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderWorkout {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_workout, parent, false)
        return ViewHolderWorkout(v, viewHolderWorkoutListener)
    }

    override fun onBindViewHolder(holder: ViewHolderWorkout, position: Int) {
        getItem(position)?.let { holder.setData(it) }
    }
}
