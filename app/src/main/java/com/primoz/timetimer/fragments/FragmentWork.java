package com.primoz.timetimer.fragments;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.primoz.timetimer.R;
import com.primoz.timetimer.activities.MainActivity;
import com.primoz.timetimer.extras.FontManager;
import com.primoz.timetimer.extras.MyCountDownTimer;
import com.primoz.timetimer.main.MainActivity2;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentWork#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentWork extends Fragment {
    private static final String ARG_TIME_WORK = "time_work";
    private static final String ARG_TIME_REST = "time_rest";
    private static final String ARG_ROUNDS = "totalRounds";
    private static final String ARG_SECONDS_TO_FINISH = "seconds_to_finish";
    final static String TIME_MILLIS = "time_millis";
    final static String RESTING = "resting";
    final static String PAUSE = "pause";
    final static String CURRENT_ROUNDS = "current_rounds";


    final static int MILLIS_TO_SECONDS = 1000;

    private int timeWork;
    private int timeRest;
    private int totalRounds;
    private int currentRounds = 0;
    private int secondsToFinish;
    private boolean rest = false;
    private boolean pause = false;
    private MyCountDownTimer myCountDownTimer;


    private Button btnPause;
    ConstraintLayout backgroundFragment;
    TextView tvRoundsLeft;
    TextView tvTimeLeft;
    TextView tvState;
    private Handler mHandler;
    private Runnable mCounterRunnable;


    public FragmentWork() {
        // Required empty public constructor
    }

    public static FragmentWork newInstance(int timeWork, int timeRest, int rounds, int secondsToFinish) {
        FragmentWork fragment = new FragmentWork();
        Bundle args = new Bundle();
        args.putInt(ARG_TIME_WORK, timeWork);
        args.putInt(ARG_TIME_REST, timeRest);
        args.putInt(ARG_ROUNDS, rounds);
        args.putInt(ARG_SECONDS_TO_FINISH, secondsToFinish);
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentWork newInstance(int timeWork, int timeRest, int rounds) {
        FragmentWork fragment = new FragmentWork();
        Bundle args = new Bundle();
        args.putInt(ARG_TIME_WORK, timeWork);
        args.putInt(ARG_TIME_REST, timeRest);
        args.putInt(ARG_ROUNDS, rounds);
        args.putInt(ARG_SECONDS_TO_FINISH, (timeWork + timeRest) * rounds);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            timeWork = getArguments().getInt(ARG_TIME_WORK);
            timeRest = getArguments().getInt(ARG_TIME_REST);
            totalRounds = getArguments().getInt(ARG_ROUNDS);
            secondsToFinish = getArguments().getInt(ARG_SECONDS_TO_FINISH);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_work, container, false);
        tvRoundsLeft = (TextView) v.findViewById(R.id.tvRoundsLeft);
        backgroundFragment = (ConstraintLayout) v.findViewById(R.id.content_Work);
        updateRounds();

        tvTimeLeft = (TextView) v.findViewById(R.id.tvTimeLeft);
        tvState = (TextView) v.findViewById(R.id.tvStateTrain);
        btnPause = (Button) v.findViewById(R.id.btnPause);

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myCountDownTimer.isRunning()) {
                    stopTimer();
                    pause = true;
                    btnPause.setText(getContext().getString(R.string.fa_play));
                    tvState.setText(R.string.stringStatusPauseReally);
                } else {
                    startTimer();
                    pause = false;
                    btnPause.setText(getContext().getString(R.string.fa_pause));
                    tvState.setText(R.string.stringStatusWORK);
                    if (rest) tvState.setText(R.string.stringStatusREST);
                }
            }
        });

        Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.ALEGREYA_TTF);
        FontManager.markAsIconContainer(v.findViewById(R.id.content_Work), iconFont);
        iconFont = FontManager.getTypeface(

                getContext(), FontManager.FONTAWESOME);
        btnPause.setTypeface(iconFont);


        newTimer(timeWork);
        return v;
    }

    private void newTimer(int time) {
        if (myCountDownTimer != null) {
            myCountDownTimer.DestroyTimer();
        }

        myCountDownTimer = new MyCountDownTimer(time * MILLIS_TO_SECONDS, MILLIS_TO_SECONDS, getActivity(), rest);
        startTimer();
        refreshTimer();
    }

    public void updateRounds() {
        currentRounds++;
        tvRoundsLeft.setText(currentRounds + " / " + totalRounds);
    }

    public void startTimer() {
        Log.v("Resume Presseed", "RESUME");
        myCountDownTimer.Start();
    }

    public void stopTimer() {
        Log.v("Pause Presseed", "STOP");
        myCountDownTimer.Stop();
    }


    public void refreshTimer() {
        mHandler = new Handler();
        mCounterRunnable = new Runnable() {
            public void run() {
                tvTimeLeft.setText(myCountDownTimer.getCurrentTime());
                mHandler.postDelayed(this, 100);
                //  Log.v("To", myCountDownTimer.getCurrentTimeMilis() + "");
                if (myCountDownTimer.getCurrentTimeMilis() == 0 && currentRounds < totalRounds) {
                    if (rest || timeRest == 0) {
                        if (timeRest != 0) {
                            changeThemeToWorkAnim();
                        }
                        tvState.setText(R.string.stringStatusWORK);
                        rest = false;
                        newTimer(timeWork);
                        updateRounds();

                    } else if (timeRest != 0) {
                        changeThemeToRestAnim();
                        rest = true;
                        newTimer(timeRest);
                        tvState.setText(R.string.stringStatusREST);
                    }
                }
                if (myCountDownTimer.getCurrentTimeMilis() == 0 && currentRounds >= totalRounds) {
                    finished();
                }


            }


        };

        mHandler.postDelayed(mCounterRunnable, 100);
    }

    private void finished() {
        mHandler.removeCallbacksAndMessages(null);
        Context context = getContext();

        int colorFromBlue = (ContextCompat.getColor(context, R.color.colorTimeWork));
        int colorToGreen = (ContextCompat.getColor(context, R.color.colorCompleted));

        animateFromToBackground(colorFromBlue, colorToGreen, btnPause);
        tvTimeLeft.setText(R.string.stringDone);

        btnPause.setText(getString(R.string.fa_done));
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity2 mainActivity = (MainActivity2) getActivity();
                mainActivity.loadWorkoutListFragment();
            }
        });
    }

    private void changeThemeToWorkAnim() {
        Context context = getContext();

        int colorFromBackground = (ContextCompat.getColor(context, R.color.colorBackgroundRest));
        int colorToBackground = (ContextCompat.getColor(context, R.color.colorBackgroundWork));

        int colorFromText = (ContextCompat.getColor(context, R.color.colorTextRest));
        int colorToText = (ContextCompat.getColor(context, R.color.colorTextWork));

        int colorFromTime = (ContextCompat.getColor(context, R.color.colorTimeRest));
        int colorToTime = (ContextCompat.getColor(context, R.color.colorTimeWork));

        ((MainActivity) getActivity()).animateFromToBackground(colorFromBackground, colorToBackground); //Background
        animateFromToBackground(colorFromBackground, colorToBackground, backgroundFragment); //Background
        animateFromToBackground(colorFromText, colorToText, tvRoundsLeft);
        animateFromToBackground(colorFromText, colorToText, tvState);
        animateFromToBackground(colorFromTime, colorToTime, tvTimeLeft);
        animateFromToBackground(colorFromTime, colorToTime, btnPause);

    }

    private void changeThemeToRest() {
        Context context = getContext();
        ((MainActivity) getActivity()).changeToRest();

        backgroundFragment.setBackgroundColor(ContextCompat.getColor(context, R.color.colorBackgroundRest));

        tvRoundsLeft.setTextColor(ContextCompat.getColor(context, R.color.colorTextRest));
        tvState.setTextColor(ContextCompat.getColor(context, R.color.colorTextRest));

        tvTimeLeft.setTextColor(ContextCompat.getColor(context, R.color.colorTimeRest));
        btnPause.setBackgroundColor(ContextCompat.getColor(context, R.color.colorTimeRest));
    }

    private void changeThemeToRestAnim() {
        /*From Work to REST*/
        Context context = getContext();

        int colorFromBackground = (ContextCompat.getColor(context, R.color.colorBackgroundWork));
        int colorToBackground = (ContextCompat.getColor(context, R.color.colorBackgroundRest));


        int colorFromText = (ContextCompat.getColor(context, R.color.colorTextWork));
        int colorToText = (ContextCompat.getColor(context, R.color.colorTextRest));

        int colorFromTime = (ContextCompat.getColor(context, R.color.colorTimeWork));
        int colorToTime = (ContextCompat.getColor(context, R.color.colorTimeRest));


        ((MainActivity) getActivity()).animateFromToBackground(colorFromBackground, colorToBackground); //Background
        animateFromToBackground(colorFromBackground, colorToBackground, backgroundFragment); //Background
        animateFromToBackground(colorFromText, colorToText, tvRoundsLeft);
        animateFromToBackground(colorFromText, colorToText, tvState);
        animateFromToBackground(colorFromTime, colorToTime, tvTimeLeft);
        animateFromToBackground(colorFromTime, colorToTime, btnPause);
    }

    private void animateFromToBackground(int colorFrom, int colorTo, final View view) {

        ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
        colorAnimation.setDuration(300); // milliseconds
        colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (view instanceof ConstraintLayout || view instanceof Button) {
                    view.setBackgroundColor((int) animator.getAnimatedValue());
                } else if (view instanceof TextView) {
                    ((TextView) view).setTextColor((int) animator.getAnimatedValue());
                }
            }
        });
        colorAnimation.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            restoreTimer();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        saveTimer(myCountDownTimer.getCurrentTimeMilis());
    }

    private void saveTimer(long currentTimeMilis) {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(TIME_MILLIS, currentTimeMilis);
        editor.putBoolean(RESTING, rest);
        editor.putBoolean(PAUSE, pause);
        editor.putInt(CURRENT_ROUNDS, currentRounds);
        editor.commit();
    }

    private void restoreTimer() {
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        long timer = sharedPref.getLong(TIME_MILLIS, 0);
        currentRounds = sharedPref.getInt(CURRENT_ROUNDS, 1);
        currentRounds--;
        updateRounds();

        rest = sharedPref.getBoolean(RESTING, false);
        pause = sharedPref.getBoolean(PAUSE, false);
        myCountDownTimer.setMillisInFuture(timer);
        if (rest) {
            changeThemeToRest();
            tvState.setText(R.string.stringStatusREST);
        }

        if (pause) {
            stopTimer();
            btnPause.setText(getContext().getString(R.string.fa_play));
            tvState.setText(R.string.stringStatusPauseReally);
        }
    }

    @Override
    public void onDestroy() {
        Log.v("Destroyed", "FragmentWork");
        myCountDownTimer.DestroyTimer();

     //   int colorToBackground = (ContextCompat.getColor(getContext(), R.color.colorBackgroundWork));
     //   ((MainActivity2) getActivity()).animateFromToBackground(colorToBackground, colorToBackground); //Background
        super.onDestroy();
    }


}
