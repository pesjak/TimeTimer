package com.primoz.timetimer.helpers

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity

/**
 * Created by Primož on 21/04/2018.
 */

fun AppCompatActivity.addFragment(fragment: Fragment, frameID: Int) {
    supportFragmentManager.inTransaction { add(frameID, fragment).addToBackStack(null) }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameID: Int) {
    supportFragmentManager.inTransaction { replace(frameID, fragment) }
}