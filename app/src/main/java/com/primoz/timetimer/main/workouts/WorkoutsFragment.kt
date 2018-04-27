package com.primoz.timetimer.main.workouts


import android.opengl.Visibility
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.primoz.timetimer.R
import com.primoz.timetimer.main.MainActivity2
import com.primoz.timetimer.adapters.MyWorkoutsAdapter
import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutsRepository
import com.primoz.timetimer.data_mvp.source.WorkoutsDataSourceDB
import com.primoz.timetimer.interfaces.ViewHolderWorkoutListener
import com.primoz.timetimer.managers.MyLinearLayoutManager
import com.primoz.timetimer.viewholders.ViewHolderWorkout
import io.realm.RealmList
import kotlinx.android.synthetic.main.fragment_workouts.*


class WorkoutsFragment : Fragment(), WorkoutsContract.View, ViewHolderWorkoutListener {
    private lateinit var mPresenter: WorkoutsContract.Presenter
    private var adapter: MyWorkoutsAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_workouts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = WorkoutsPresenter(WorkoutsRepository.getInstance(WorkoutsDataSourceDB.getInstance()), this)

        rvWorkouts.setHasFixedSize(true)
        rvWorkouts.layoutManager = MyLinearLayoutManager(context)
        val touchHelperCallback = TouchHelperCallback()
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(rvWorkouts)

        fabAddWorkout.setOnClickListener { mPresenter.addNewWorkout() }
        errorLayout.setOnClickListener { mPresenter.addNewWorkout() }
    }

    override fun onResume() {
        super.onResume()
        mPresenter.start()
    }

    override fun showWorkouts(workouts: RealmList<Workout>) {
        if (adapter == null) {
            adapter = MyWorkoutsAdapter(workouts, this)
        }
        rvWorkouts.adapter = adapter
        if (workouts.isEmpty()) {
            showNoWorkouts()
        } else {
            errorLayout.visibility = View.GONE
            rvWorkouts.visibility = View.VISIBLE
        }
    }

    override fun onEditClicked(item: Workout) {
        mPresenter.editWorkout(item.workoutID)
    }

    override fun onItemClicked(item: Workout) {
        mPresenter.startWorkout(item.workoutID)
    }


    override fun showNoWorkouts() {
        errorLayout.visibility = View.VISIBLE
        rvWorkouts.visibility = View.GONE
    }

    override fun showAddWorkout() {
        (activity as MainActivity2).loadNewPrepareFragment()
    }

    override fun showWorkoutDetailUI(workoutID: Int) {
        (activity as MainActivity2).loadEditPrepareFragment(workoutID)
    }

    override fun showStartWorkout(workoutID: Int) {
        (activity as MainActivity2).showPlayFragment(workoutID)
    }

    override fun showSuccessfullySavedMessage() {
        view?.let { Snackbar.make(it, "Successfully saved", Snackbar.LENGTH_LONG).show() }
    }

    override fun showSuccessfullyDeletedMessage() {
        view?.let { Snackbar.make(it, "Successfully deleted", Snackbar.LENGTH_LONG).show() }
    }

    override fun setPresenter(presenter: WorkoutsContract.Presenter) {
        mPresenter = presenter
    }

    private inner class TouchHelperCallback internal constructor() : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val workoutViewHolder = viewHolder as ViewHolderWorkout
            mPresenter.removeWorkout(workoutViewHolder.item.workoutID)
            mPresenter.start()
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }
    }
}
