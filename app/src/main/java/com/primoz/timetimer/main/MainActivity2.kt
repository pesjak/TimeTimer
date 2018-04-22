package com.primoz.timetimer.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.primoz.timetimer.R
import com.primoz.timetimer.helpers.addFragment
import com.primoz.timetimer.helpers.replaceFragment
import com.primoz.timetimer.main.prepare.PrepareFragment
import com.primoz.timetimer.main.workouts.WorkoutsFragment


/**
 * Created by Primož on 21/04/2018.
 */

class MainActivity2 : AppCompatActivity() {
    companion object {
    val KEY_ID_PROGRAM = "ProgramID"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        replaceFragment(WorkoutsFragment(), R.id.fragment_container)
    }

    fun loadWorkoutListFragment() {
        replaceFragment(WorkoutsFragment(), R.id.fragment_container)
    }

    fun loadNewPrepareFragment() {
        addFragment(PrepareFragment(), R.id.fragment_container)
    }

    fun loadEditPrepareFragment(idOfProgram: Int) {
        val bundle = Bundle()
        bundle.putInt(KEY_ID_PROGRAM, idOfProgram)
        val prepareFragment = PrepareFragment()
        prepareFragment.arguments = bundle
        addFragment(prepareFragment, R.id.fragment_container)
    }

    fun showPlayFragment(idOfProgram: Int) {
        val bundle = Bundle()
        bundle.putInt(KEY_ID_PROGRAM, idOfProgram)
        val prepareFragment = PrepareFragment()
        prepareFragment.arguments = bundle
        addFragment(prepareFragment, R.id.fragment_container)
    }

}