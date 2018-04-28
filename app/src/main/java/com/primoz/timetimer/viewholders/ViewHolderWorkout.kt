package com.primoz.timetimer.viewholders

import android.support.v7.widget.RecyclerView
import android.view.View
import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.extras.Util
import com.primoz.timetimer.interfaces.ViewHolderWorkoutListener
import kotlinx.android.synthetic.main.item_workout.view.*

/**
 * Created by Primož on 21/08/2017.
 */

class ViewHolderWorkout(itemView: View, private var listener: ViewHolderWorkoutListener) : RecyclerView.ViewHolder(itemView) {

    lateinit var item: Workout

    fun getData(): Workout {
        return item
    }

    fun setData(item: Workout) {
        this.item = item

        itemView.tvName.text = item.name
        itemView.tvTotalTime.text = getTime(item.totalSessionSeconds)
        itemView.tvWorkTime.text = getTime(item.timeWork)
        itemView.tvRestTime.text = getTime(item.timeRest)
        itemView.tvRounds.text = "${item.timeRounds}X"

        itemView.btnEdit.setOnClickListener { listener.onEditClicked(item) }
        itemView.cvWorkoutItem.setOnClickListener { listener.onItemClicked(item) }
    }

    private fun getTime(totalSeconds: Int): String {
        val seconds = Util.getSeconds(totalSeconds)
        val minutes = Util.getMinutes(totalSeconds)
        val hours = Util.getHours(totalSeconds)
        val timeString: String
        if (hours <= 0) {
            timeString = String.format("%02d:%02d", minutes, seconds)
        } else {
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
        return timeString
    }


}
