package com.primoz.timetimer.main

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.WindowManager
import com.primoz.timetimer.R
import com.primoz.timetimer.data_mvp.Workout
import com.primoz.timetimer.data_mvp.WorkoutsDataSource
import com.primoz.timetimer.data_mvp.source.DataHelper
import com.primoz.timetimer.data_mvp.source.WorkoutsDataSourceDB
import com.primoz.timetimer.fragments.FragmentWork
import com.primoz.timetimer.helpers.addFragment
import com.primoz.timetimer.helpers.replaceFragment
import com.primoz.timetimer.main.prepare.PrepareFragment
import com.primoz.timetimer.main.workouts.WorkoutsFragment


/**
 * Created by Primož on 21/04/2018.
 */

class MainActivity2 : AppCompatActivity() {
    // Maximumn sound stream.
    private val MAX_STREAMS = 2
    // Stream type.
    private val streamType = AudioManager.STREAM_MUSIC

    lateinit var soundPool: SoundPool
    var loaded = false
    var soundIDone: Int = 0
    var soundIDFinishedRound: Int = 0
    private var volume: Float = 0f


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
        WorkoutsDataSourceDB.getInstance().getWorkout(idOfProgram, object : WorkoutsDataSource.GetWorkoutCallback {
            override fun onWorkoutLoaded(workout: Workout) {
                val prepareFragment = FragmentWork.newInstance(workout.timeWork, workout.timeRest, workout.timeRounds)
                addFragment(prepareFragment, R.id.fragment_container)
            }

            override fun onDataNotAvailable() {
            }
        })
    }

    fun showPlayFragment(title: String, workSeconds: Int, restSeconds: Int, rounds: Int) {
        val prepareFragment = FragmentWork.newInstance(workSeconds, restSeconds, rounds)
        addFragment(prepareFragment, R.id.fragment_container)
    }


    private fun initAudio() {
        volume = getVolume()

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.volumeControlStream = streamType

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21) {

            val audioAttrib = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()

            val builder = SoundPool.Builder()
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS)

            soundPool = builder.build()
        } else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            soundPool = SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0)
        }// for Android SDK < 21

        // When Sound Pool load complete.
        soundPool.setOnLoadCompleteListener { soundPool, sampleId, status -> loaded = true }


        soundIDone = soundPool.load(this, R.raw.finished, 1)
        soundIDFinishedRound = soundPool.load(this, R.raw.finnished_round, 1)


    }

    private fun getVolume(): Float {
        // AudioManager audio settings for adjusting the volume
        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        // Current volumn Index of particular stream type.
        val currentVolumeIndex = audioManager.getStreamVolume(streamType).toFloat()

        // Get the maximum volume index for a particular stream type.
        val maxVolumeIndex = audioManager.getStreamMaxVolume(streamType).toFloat()

        // Volumn (0 --> 1)
        return currentVolumeIndex / maxVolumeIndex
    }

    /*Plays end of round if soundpool is ready and is 1 second remaining*/
    fun playEndRoundSound(seconds: Long, rest: Boolean) {
        if (loaded && seconds == 1L) {
            volume = getVolume()
            if (rest) {
                soundPool.play(soundIDFinishedRound, volume, volume, 1, 0, 1f)
            } else {
                soundPool.play(soundIDone, volume, volume, 1, 0, 1f)
            }
        }
    }
}