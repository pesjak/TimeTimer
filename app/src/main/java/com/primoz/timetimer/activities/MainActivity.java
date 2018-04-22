package com.primoz.timetimer.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.primoz.timetimer.R;
import com.primoz.timetimer.extras.FontManager;
import com.primoz.timetimer.fragments.FragmentWork;
import com.primoz.timetimer.interfaces.OnBackPressedInterface;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    final static String FRAGMENT_WORK = "fragmentWork";
    // Maximumn sound stream.
    private static final int MAX_STREAMS = 2;
    // Stream type.
    private static final int streamType = AudioManager.STREAM_MUSIC;

    public static SoundPool soundPool;
    public static boolean loaded = false;
    public static int soundIDone;
    public static int soundIDFinishedRound;

    CoordinatorLayout rlBackground;

    private float volume;
    private OnBackPressedInterface onBackPressedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rlBackground = (CoordinatorLayout) findViewById(R.id.rlBackground);

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);

        TextView title = (TextView) findViewById(R.id.tvTitle);



        FragmentManager manager = getSupportFragmentManager();
        fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            //fragment = FragmentMain.newInstance();
            manager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        initAudio();


    }


    private void initAudio() {
        volume = getVolume();

        // Suggests an audio stream whose volume should be changed by
        // the hardware volume controls.
        this.setVolumeControlStream(streamType);

        // For Android SDK >= 21
        if (Build.VERSION.SDK_INT >= 21) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            soundPool = builder.build();
        }
        // for Android SDK < 21
        else {
            // SoundPool(int maxStreams, int streamType, int srcQuality)
            soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

        // When Sound Pool load complete.
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                loaded = true;
            }
        });


        soundIDone = soundPool.load(this, R.raw.finished, 1);
        soundIDFinishedRound = soundPool.load(this, R.raw.finnished_round, 1);


    }

    private float getVolume() {
        // AudioManager audio settings for adjusting the volume
        AudioManager audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);

        // Current volumn Index of particular stream type.
        float currentVolumeIndex = (float) audioManager.getStreamVolume(streamType);

        // Get the maximum volume index for a particular stream type.
        float maxVolumeIndex = (float) audioManager.getStreamMaxVolume(streamType);

        // Volumn (0 --> 1)
        return currentVolumeIndex / maxVolumeIndex;
    }


/*
    public void loadMainFragment() {
        FragmentManager manager = getSupportFragmentManager();
        fragment = FragmentMain.newInstance();
        getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        manager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public void loadWork(int timeTrain, int timeRest, int rounds, int secondsToFinish) {
        FragmentWork fragmentWork = FragmentWork.newInstance(timeTrain, timeRest, rounds, secondsToFinish);
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragmentWork)
                .addToBackStack(null)
                .commit();
    }

    public void loadPrepare(String name, int timeTrain, int timeRest, int rounds) {
        FragmentPrepare fragmentPrepare = FragmentPrepare.newInstance();
        fragmentPrepare.saveEdit(this, name, timeTrain, timeRest, rounds);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentPrepare)
                .addToBackStack(null)
                .commit();
    }

    public void loadPrepare(boolean clearSharedPreferences) {
        if (clearSharedPreferences) clearSharedPreferences();
        FragmentPrepare fragmentPrepare = FragmentPrepare.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragmentPrepare)
                .addToBackStack(null)
                .commit();
    }
*/

    private void clearSharedPreferences() {
        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }


    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null) {
            onBackPressedListener.onBackPressedInFragment();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }

    /*Plays end of round if soundpool is ready and is 1 second remaining*/
    public void playEndRoundSound(long seconds, boolean rest) {
        if (loaded && seconds == 1) {
            volume = getVolume();
            if (rest) {
                soundPool.play(soundIDFinishedRound, volume, volume, 1, 0, 1f);
            } else {
                soundPool.play(soundIDone, volume, volume, 1, 0, 1f);
            }
        }
    }

    public void setOnBackPressedListener(OnBackPressedInterface onBackPressedListener) {
        this.onBackPressedListener = onBackPressedListener;
    }

    public void unsetOnBackPressedListener() {
        this.onBackPressedListener = null;
    }

    public void changeToRest() {
        rlBackground.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.colorBackgroundRest));
    }

    public void animateFromToBackground(int colorFrom, int colorTo) {
        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(300); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                rlBackground.setBackgroundColor((int) animator.getAnimatedValue());
            }
        });
        colorAnimation.start();
    }
}
