package com.primoz.timetimer.extras;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import com.primoz.timetimer.activities.MainActivity;

/**
 * Created by primo on 18. 10. 2016.
 */
public class MyCountDownTimer {
    private long millisInFuture;
    private long countDownInterval;
    private boolean status;
    private Handler mHandler;
    private Runnable mCounterRunnable;
    Activity activity;
    boolean rest;

    public MyCountDownTimer(long pMillisInFuture, long pCountDownInterval, Activity activity, boolean rest) {
        this.millisInFuture = pMillisInFuture;
        this.countDownInterval = pCountDownInterval;
        this.activity = activity;
        this.rest = rest;
        status = false;
        Initialize();
    }

    public void setMillisInFuture(long millisInFuture) {
        this.millisInFuture = millisInFuture;
    }

    public String getCurrentTime() {
        int totalSeconds = (int) ((millisInFuture / (double) 1000) + 0.1);
        int minutes = Util.getMinutes(totalSeconds);
        int seconds = Util.getSeconds(totalSeconds);
        String sMin = String.valueOf(minutes);
        return String.format(sMin + ":" + "%02d", seconds);
    }

    public long getCurrentTimeMilis() {
        return millisInFuture;
    }

    public void Start() {
        status = true;
    }

    public void Stop() {
        status = false;
    }

    public boolean isRunning() {
        return status;
    }

    public void Initialize() {
        mHandler = new Handler();
        Log.v("Status", "Starting CountDown");
        mCounterRunnable = new Runnable() {
            public void run() {
                long sec = millisInFuture / 1000;
                if (status) {
                    if (millisInFuture <= 0) {
                        Log.v("Status", "DONE");
                    } else {
                        millisInFuture -= countDownInterval;
                        mHandler.postDelayed(this, countDownInterval);

                        if (sec >= 0 && sec <= 5) {
                            MainActivity mainActivity = (MainActivity) activity;
                            mainActivity.playEndRoundSound(sec, rest);
                        }

                    }
                } else {
                    Log.v("status", Long.toString(sec) + " left and stopped.");
                    mHandler.postDelayed(this, countDownInterval);
                }
            }
        };

        mHandler.postDelayed(mCounterRunnable, countDownInterval);
    }

    public void DestroyTimer() {
        mHandler.removeCallbacks(mCounterRunnable);
    }

}


