package com.primoz.timetimer.helpers

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction


/**
 * Created by Primož on 21/04/2018.
 */

inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}