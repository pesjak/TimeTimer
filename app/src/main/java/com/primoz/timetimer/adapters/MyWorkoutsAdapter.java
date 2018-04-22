package com.primoz.timetimer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.primoz.timetimer.R;
import com.primoz.timetimer.data_mvp.Workout;
import com.primoz.timetimer.interfaces.ViewHolderWorkoutListener;
import com.primoz.timetimer.viewholders.ViewHolderWorkout;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by Primož on 21/08/2017.
 */

public class MyWorkoutsAdapter extends RealmRecyclerViewAdapter<Workout, ViewHolderWorkout> {

    private ViewHolderWorkoutListener viewHolderWorkoutListener;

    public MyWorkoutsAdapter(OrderedRealmCollection<Workout> data, ViewHolderWorkoutListener listener) {
        super(data, true);
        viewHolderWorkoutListener = listener;
    }

    @Override
    public ViewHolderWorkout onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new ViewHolderWorkout(v, viewHolderWorkoutListener);
    }

    @Override
    public void onBindViewHolder(ViewHolderWorkout holder, int position) {
        holder.setItem(getItem(position));
    }
}
