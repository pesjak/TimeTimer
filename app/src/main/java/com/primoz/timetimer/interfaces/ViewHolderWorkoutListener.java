package com.primoz.timetimer.interfaces;


import com.primoz.timetimer.data_mvp.Workout;

/**
 * Created by Primož on 21/08/2017.
 */

public interface ViewHolderWorkoutListener {
    void onEditClicked(Workout item);
    void onItemClicked(Workout item);
}
