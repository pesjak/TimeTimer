package com.primoz.timetimer.main.prepare

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.text.InputFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.primoz.timetimer.R
import com.primoz.timetimer.data_mvp.WorkoutsRepository
import com.primoz.timetimer.data_mvp.source.WorkoutsDataSourceDB
import com.primoz.timetimer.extras.FontManager
import com.primoz.timetimer.main.MainActivity
import kotlinx.android.synthetic.main.fragment_prepare.*


class PrepareFragment : Fragment(), PrepareContract.View, TotalTimeListener {
    lateinit var mPresenter: PrepareContract.Presenter
    private var idOfProgram: Int = 0
    var NAME = "name"
    var WORK = "train"
    var REST = "rest"
    var ROUNDS = "rounds"

    private var dialogShown: Boolean = false

    var dialogClickListener: DialogInterface.OnClickListener = DialogInterface.OnClickListener { dialog, which ->
        when (which) {
            DialogInterface.BUTTON_POSITIVE -> {
                mPresenter.saveProgram(etName.text.toString(), programView.getWorkSeconds(), programView.getRestSeconds(), programView.getRounds())
                dialogShown = true
            }
            DialogInterface.BUTTON_NEGATIVE -> {
                dialogShown = true
                (activity as MainActivity).onBackPressed()
            }
        }
    }

    override fun showAlreadyExists() {
        Toast.makeText(context, getString(R.string.error_already_exists), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccesfullyAddedNewWorkout() {
        Toast.makeText(context, getString(R.string.success_add), Toast.LENGTH_SHORT).show()
    }

    override fun showSuccesfullyEditedWorkout() {
        Toast.makeText(context, getString(R.string.success_edit), Toast.LENGTH_SHORT).show()
    }

    override fun showWork(work: Int) {
        programView.setWork(work)
    }

    override fun showRest(rest: Int) {
        programView.setRest(rest)
    }

    override fun showRounds(rounds: Int) {
        programView.setRounds(rounds)
    }

    override fun showTotalTime(time: String) {
        cTotalTime.text = time
    }

    override fun showTitle(title: String) {
        etName.setText(title, TextView.BufferType.EDITABLE)
    }

    override fun updateTotalTime(hours: Int, minutes: Int, seconds: Int) {
        mPresenter.loadTotalTime(hours, minutes, seconds)
    }

    override fun showDialogAbort() {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(R.string.string_do_you_want_to_save)
                .setPositiveButton(R.string.string_yes, dialogClickListener)
                .setNegativeButton(R.string.string_no, dialogClickListener).show()
    }

    override fun setPresenter(presenter: PrepareContract.Presenter) {
        this.mPresenter = presenter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_prepare, container, false)
    }

    override fun showPlayFragment(idOfProgram: Int) {
        (activity as MainActivity).showPlayFragment(idOfProgram)
    }

    private fun showPlayFragment(title: String, workSeconds: Int, restSeconds: Int, rounds: Int) {
        (activity as MainActivity).showPlayFragment(title, workSeconds, restSeconds, rounds)
    }

    override fun showEnterTitle() {
        Toast.makeText(context, getString(R.string.error_enter_title), Toast.LENGTH_SHORT).show()
    }

    override fun closePrepare() {
        (activity as MainActivity).loadWorkoutListFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retainInstance = true

        PreparePresenter(WorkoutsRepository.getInstance(WorkoutsDataSourceDB.getInstance()), this)

        //Edit Text Font
        etName.filters = arrayOf<InputFilter>(InputFilter.AllCaps())
        etName.typeface = FontManager.getTypeface(context, FontManager.ALEGREYA_TTF)


        if (savedInstanceState != null) {
            val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
            val name = sharedPref.getString(NAME, "Enter the title here")
            val train = sharedPref.getInt(WORK, 5)
            val rest = sharedPref.getInt(REST, 0)
            val rounds = sharedPref.getInt(ROUNDS, 1)
            showTitle(name)
            showWork(train)
            showRest(rest)
            showRounds(rounds)
            updateTotalTime(0, 0, 5)
        } else {
            //Load Program if Necessary
            if (arguments != null) {
                idOfProgram = arguments!!.getInt(MainActivity.KEY_ID_PROGRAM)
                mPresenter.loadProgram(idOfProgram)
            }else{
                showWork(5)
                updateTotalTime(0,0,5)
            }
        }
        //On Click Listeners
        btnSave.setOnClickListener { mPresenter.saveProgram(etName.text.toString(), programView.getWorkSeconds(), programView.getRestSeconds(), programView.getRounds()) }
        btnStart.setOnClickListener {
            if (etName.text.isEmpty()) {
                showEnterTitle()
                return@setOnClickListener
            }

            mPresenter.saveProgram(etName.text.toString(), programView.getWorkSeconds(), programView.getRestSeconds(), programView.getRounds())

            showPlayFragment(etName.text.toString(), programView.getWorkSeconds(), programView.getRestSeconds(), programView.getRounds())

        }
        programView.setUpdateListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        //Save the fragment's state here
        if (etName != null) saveTimes(etName.text.toString(), programView.getWorkSeconds(), programView.getRestSeconds(), programView.getRounds())
    }

    fun saveTimes(name: String, timeTrain: Int, timeRest: Int, rounds: Int) {
        val sharedPref = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = sharedPref.edit()
        editor.putString(NAME, name)
        editor.putInt(WORK, timeTrain)
        editor.putInt(REST, timeRest)
        editor.putInt(ROUNDS, rounds)
        editor.apply()
    }

    fun allowBackPressed(): Boolean {
        if (!dialogShown) {
            showDialogAbort()
        }
        return dialogShown
    }


}