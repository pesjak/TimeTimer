/*
package com.primoz.timetimer.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.primoz.timetimer.R;
import com.primoz.timetimer.activities.MainActivity;
import com.primoz.timetimer.adapters.MyWorkoutsAdapter;
import com.primoz.timetimer.managers.MyLinearLayoutManager;
import com.primoz.timetimer.realmHelper.DataHelper;
import com.primoz.timetimer.realmObjects.Workout;
import com.primoz.timetimer.realmObjects.WorkoutList;
import com.primoz.timetimer.viewholders.ViewHolderWorkout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;
import io.realm.RealmList;

public class FragmentMain extends Fragment {
    private static final String TAG = "FragmentMain";
    @BindView(R.id.rvWorkouts)
    RecyclerView rvWorkouts;
    Unbinder unbinder;
    @BindView(R.id.errorLayout)
    TextView tvNothingYet;
    private Realm realm;
    private Context context;

    MyWorkoutsAdapter adapter;

    public FragmentMain() {
    }

    public static FragmentMain newInstance() {
        return new FragmentMain();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        context = view.getContext();
        initRecyclerView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (rvWorkouts != null) {
            rvWorkouts.setAdapter(null);
        }
        realm.close();
    }

    @OnClick(R.id.fabAddWorkout)
    public void onClick() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.loadPrepare(true);
    }


    private void initRecyclerView() {
        final RealmList<Workout> workoutRealmResults = realm.where(WorkoutList.class).findFirst().getWorkoutRealmList();
        hideErrorIfSomethingisAdded(workoutRealmResults);
        rvWorkouts.setLayoutManager(new MyLinearLayoutManager(context));
        adapter = new MyWorkoutsAdapter(workoutRealmResults,getActivity());
        rvWorkouts.setAdapter(adapter);
        rvWorkouts.setHasFixedSize(true);

        TouchHelperCallback touchHelperCallback = new TouchHelperCallback();
        ItemTouchHelper touchHelper = new ItemTouchHelper(touchHelperCallback);
        touchHelper.attachToRecyclerView(rvWorkouts);
    }

    private void hideErrorIfSomethingisAdded(RealmList<Workout> workoutRealmResults) {
        if (workoutRealmResults.size() > 0) {
            tvNothingYet.setVisibility(View.GONE);
        }else{
            tvNothingYet.setVisibility(View.VISIBLE);
        }
    }

    private class TouchHelperCallback extends ItemTouchHelper.SimpleCallback {

        TouchHelperCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return true;
        }

        @Override
        public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
            DataHelper.deleteItemAsync(realm, ((ViewHolderWorkout) viewHolder).getItem().getName(),adapter);
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
    }
}
*/
