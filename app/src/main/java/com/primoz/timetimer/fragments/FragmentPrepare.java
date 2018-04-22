/*
package com.primoz.timetimer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.primoz.timetimer.R;
import com.primoz.timetimer.activities.MainActivity;
import com.primoz.timetimer.data_mvp.source.DataHelper;
import com.primoz.timetimer.main.MainActivity2;
import com.primoz.timetimer.extras.FontManager;
import com.primoz.timetimer.extras.Util;
import com.primoz.timetimer.interfaces.OnBackPressedInterface;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.realm.Realm;

public class FragmentPrepare extends Fragment implements OnBackPressedInterface {
    final static int TO_CHANGE = 5;
    public static final int REPEAT_DELAY = 75;

    @BindView(R.id.tvRemoveTimeTrain)
    TextView tvRemoveTimeTrain;
    @BindView(R.id.cTimeTrain)
    Chronometer cTimeTrain;
    @BindView(R.id.tvAddTimeTrain)
    TextView tvAddTimeTrain;
    @BindView(R.id.tvRemoveTimeRest)
    TextView tvRemoveTimeRest;
    @BindView(R.id.cTimeRest)
    Chronometer cTimeRest;
    @BindView(R.id.tvAddTimeRest)
    TextView tvAddTimeRest;
    @BindView(R.id.tvRemoveRound)
    TextView tvRemoveRound;
    @BindView(R.id.tvRounds)
    TextView tvRounds;
    @BindView(R.id.tvAddRound)
    TextView tvAddRound;
    @BindView(R.id.cTotalTime)
    Chronometer cTotalTime;
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnSave)
    Button btnSave;
    @BindView(R.id.etName)
    EditText etName;

    Unbinder unbinder;
    private Handler repeatUpdateHandler = new Handler();
    private boolean mAutoIncrement = false;
    private boolean mAutoDecrement = false;

    final static String NAME = "name";
    final static String TRAIN = "train";
    final static String REST = "rest";
    final static String ROUNDS = "rounds";
    private Realm realm;
    private String oldName;

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    if (saveWorkout()) {
                        unsetOnBackListener();
                        ((MainActivity2) getActivity()).onBackPressed();
                    }
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    unsetOnBackListener();
                    ((MainActivity2) getActivity()).onBackPressed();
                    break;
            }
        }
    };


    public FragmentPrepare() {
    }

    public static FragmentPrepare newInstance() {
        return new FragmentPrepare();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            //Restore the fragment's state here
            initCustomWork();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's state here
        if (etName != null)
            saveTimes(getName(), getCurrentTimeFromWork(), getCurrentTimeFromRest(), getCurrentRounds());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        View v = inflater.inflate(R.layout.fragment_prepare, container, false);
        unbinder = ButterKnife.bind(this, v);
        settingListenersForTrain();
        settingListenersForRest();
        settingListenersForRounds();

        initEditTextOnEnterHideKeyboard();
        initCustomWork();

        Typeface iconFont = FontManager.getTypeface(getContext(), FontManager.ALEGREYA_TTF);
        FontManager.markAsIconContainer(v.findViewById(R.id.fragment_content), iconFont);
        iconFont = FontManager.getTypeface(getContext(), FontManager.FONTAWESOME);
        btnStart.setTypeface(iconFont);
        btnSave.setTypeface(iconFont);
        ((MainActivity) getActivity()).setOnBackPressedListener(this);

        return v;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        realm.close();
        unsetOnBackListener();
    }

    private void unsetOnBackListener() {
        ((MainActivity) getActivity()).unsetOnBackPressedListener();
    }

    @Override
    public void onBackPressedInFragment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.string_do_you_want_to_save).setPositiveButton(R.string.string_yes, dialogClickListener)
                .setNegativeButton(R.string.string_no, dialogClickListener).show();
    }


    @OnClick(R.id.etName)
    public void onClickShowCursor() {
        etName.setCursorVisible(true);
    }

    @OnClick(R.id.btnStart)
    public void onClick() {
        int timeTrain = getCurrentTimeFromWork();
        int timeRest = getCurrentTimeFromRest();
        int rounds = getCurrentRounds();
        int secondsToFinish = getTotalSessionSeconds();
        saveWorkout();

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.loadWork(timeTrain, timeRest, rounds, secondsToFinish);
    }

    @OnClick(R.id.btnSave)
    public void onClickSave() {
        if (saveWorkout()) {
            ((MainActivity) getActivity()).loadMainFragment();
        }
    }

    private boolean saveWorkout() {
        if (checkName()) {
            DataHelper.addItem(realm, getOldName(), getName(), getTotalSessionSeconds(), getCurrentTimeFromWork(), getCurrentTimeFromRest(), getCurrentRounds());
            return true;
        }
        Toast.makeText(getActivity(), R.string.string_error_please_enter_a_new_name, Toast.LENGTH_SHORT).show();
        return false;
    }

    private boolean checkName() {
        String checkName = etName.getText().toString().replaceAll("\\s+", "");
        return checkName.length() > 0;
    }


//Increasing seconds
    private void increase(int idTextView) {

        switch (idTextView) {
            case R.id.cTimeTrain: {
                int totalSeconds = getCurrentTimeFromWork();
                totalSeconds += TO_CHANGE;
                updateTimeTrain(totalSeconds);
                break;
            }
            case R.id.cTimeRest: {
                int totalSeconds = getCurrentTimeFromRest();
                totalSeconds += TO_CHANGE;
                updateTimeRest(totalSeconds);
                break;
            }
            case R.id.tvRounds: {
                int rounds = getCurrentRounds();
                rounds += 1;
                updateRounds(rounds);
                break;
            }
        }
        updateTotalTime();
    }

//Decreasing seconds
    private void decrease(int idTextView) {
        switch (idTextView) {
            case R.id.cTimeTrain: {
                int totalSeconds = getCurrentTimeFromWork();
                if (totalSeconds > 5) {
                    totalSeconds -= TO_CHANGE;
                    updateTimeTrain(totalSeconds);
                }
                break;
            }
            case R.id.cTimeRest: {
                int totalSeconds = getCurrentTimeFromRest();
                if (totalSeconds > 0) {
                    totalSeconds -= TO_CHANGE;
                    updateTimeRest(totalSeconds);
                }
                break;
            }
            case R.id.tvRounds: {
                int rounds = getCurrentRounds();
                if (rounds > 1) {
                    rounds -= 1;
                    updateRounds(rounds);
                }
                break;
            }
        }
        updateTotalTime();
    }

    private void updateTotalTime() {
        int totalSeconds = getTotalSessionSeconds();
        int seconds = Util.getSeconds(totalSeconds);
        int minutes = Util.getMinutes(totalSeconds);
        int hours = Util.getHours(totalSeconds);
        String timeString;
        if (hours <= 0) {
            timeString = String.format("%02d:%02d", minutes, seconds);
        } else {
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        cTotalTime.setText(timeString);

        saveTimes(getName(), getCurrentTimeFromWork(), getCurrentTimeFromRest(), getCurrentRounds());
    }

    private void initEditTextOnEnterHideKeyboard() {
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                etName.setCursorVisible(false);
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(etName.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    private void updateTimeTrain(int totalSeconds) {
        int seconds = Util.getSeconds(totalSeconds);
        int minutes = Util.getMinutes(totalSeconds);
        String timeString = String.format("%02d:%02d", minutes, seconds);
        Log.v("Current Time Train", timeString);
        cTimeTrain.setText(timeString);
    }

    private void updateTimeRest(int totalSeconds) {
        int seconds = Util.getSeconds(totalSeconds);
        int minutes = Util.getMinutes(totalSeconds);
        String timeString = String.format("%02d:%02d", minutes, seconds);
        Log.v("Current Time Rest", timeString);
        cTimeRest.setText(timeString);
    }

    private void updateRounds(int rounds) {
        tvRounds.setText(String.valueOf(rounds));
    }


    public void saveTimes(String name, int timeTrain, int timeRest, int rounds) {
        Log.v("Saving state", "");

        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NAME, name);
        editor.putInt(TRAIN, timeTrain);
        editor.putInt(REST, timeRest);
        editor.putInt(ROUNDS, rounds);
        editor.apply();
    }

    public void saveEdit(Context context, String name, int timeTrain, int timeRest, int rounds) {
        Log.v("Saving state", "");

        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NAME, name);
        editor.putInt(TRAIN, timeTrain);
        editor.putInt(REST, timeRest);
        editor.putInt(ROUNDS, rounds);
        editor.apply();
    }

    private void initCustomWork() {
        Log.v("Restoring state", "");

        SharedPreferences sharedPref = PreferenceManager
                .getDefaultSharedPreferences(getContext());
        String name = sharedPref.getString(NAME, "Name");
        int train = sharedPref.getInt(TRAIN, 5);
        int rest = sharedPref.getInt(REST, 0);
        int rounds = sharedPref.getInt(ROUNDS, 1);
        oldName = name;
        if (name.equals("Name")) {
            etName.setHint(name);
        } else {
            etName.setText(name);
        }
        updateTimeTrain(train);
        updateTimeRest(rest);
        updateRounds(rounds);

        updateTotalTime();
    }

    private void settingListenersForTrain() {
        tvRemoveTimeTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cTimeTrain.getText() != null) {
                    decrease(cTimeTrain.getId());
                }
            }
        });

        tvRemoveTimeTrain.setOnLongClickListener(new View.OnLongClickListener() {
                                                     public boolean onLongClick(View arg0) {
                                                         mAutoDecrement = true;
                                                         repeatUpdateHandler.post(new RepeatUpdaterTrain());
                                                         return false;
                                                     }
                                                 }
        );

        tvRemoveTimeTrain.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {
                    mAutoDecrement = false;
                }
                return false;
            }
        });


        tvAddTimeTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increase(cTimeTrain.getId());
            }
        });

        tvAddTimeTrain.setOnLongClickListener(new View.OnLongClickListener() {
                                                  public boolean onLongClick(View arg0) {
                                                      mAutoIncrement = true;
                                                      repeatUpdateHandler.post(new RepeatUpdaterTrain());
                                                      return false;
                                                  }
                                              }
        );

        tvAddTimeTrain.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });
    }


    private void settingListenersForRest() {
        tvRemoveTimeRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cTimeRest.getText() != null) {
                    decrease(cTimeRest.getId());
                }
            }
        });

        tvRemoveTimeRest.setOnLongClickListener(new View.OnLongClickListener() {
                                                    public boolean onLongClick(View arg0) {
                                                        mAutoDecrement = true;
                                                        repeatUpdateHandler.post(new RepeatUpdaterRest());
                                                        return false;
                                                    }
                                                }
        );

        tvRemoveTimeRest.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {
                    mAutoDecrement = false;
                }
                return false;
            }
        });


        tvAddTimeRest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increase(cTimeRest.getId());
            }
        });

        tvAddTimeRest.setOnLongClickListener(new View.OnLongClickListener() {
                                                 public boolean onLongClick(View arg0) {
                                                     mAutoIncrement = true;
                                                     repeatUpdateHandler.post(new RepeatUpdaterRest());
                                                     return false;
                                                 }
                                             }
        );

        tvAddTimeRest.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });
    }


    private void settingListenersForRounds() {
        tvRemoveRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvRounds.getText() != null) {
                    decrease(tvRounds.getId());
                }
            }
        });

        tvRemoveRound.setOnLongClickListener(new View.OnLongClickListener() {
                                                 public boolean onLongClick(View arg0) {
                                                     mAutoDecrement = true;
                                                     repeatUpdateHandler.post(new RepeatUpdaterRounds());
                                                     return false;
                                                 }
                                             }
        );

        tvRemoveRound.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoDecrement) {
                    mAutoDecrement = false;
                }
                return false;
            }
        });


        tvAddRound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increase(tvRounds.getId());
            }
        });

        tvAddRound.setOnLongClickListener(new View.OnLongClickListener() {
                                              public boolean onLongClick(View arg0) {
                                                  mAutoIncrement = true;
                                                  repeatUpdateHandler.post(new RepeatUpdaterRounds());
                                                  return false;
                                              }
                                          }
        );

        tvAddRound.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if ((event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL)
                        && mAutoIncrement) {
                    mAutoIncrement = false;
                }
                return false;
            }
        });
    }


    @NonNull
    private int getCurrentTimeFromWork() {
        if (cTimeTrain == null) return 0;
        String time = cTimeTrain.getText().toString();
        return Util.getTotalSecondsFromString(time);
    }

    @NonNull
    private String getName() {

        return etName.getText().toString();
    }

    public String getOldName() {
        return oldName;
    }

    @NonNull
    private int getCurrentTimeFromRest() {
        if (cTimeRest == null) return 0;
        String time = cTimeRest.getText().toString();
        return Util.getTotalSecondsFromString(time);
    }

    @NonNull
    private int getCurrentRounds() {
        if (tvRounds == null) return 0;
        return Integer.valueOf(tvRounds.getText().toString());
    }

    private int getTotalSessionSeconds() {
        int training = getCurrentTimeFromWork() * getCurrentRounds();
        int rest = getCurrentTimeFromRest() * getCurrentRounds();
        if (getCurrentRounds() > 1) {
            return training + rest;
        }
        return training;
    }


We use RepeatUpdater class for auto-incrementing on button hold

    private class RepeatUpdaterTrain implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                increase(cTimeTrain.getId());
                repeatUpdateHandler.postDelayed(new RepeatUpdaterTrain(), REPEAT_DELAY);
            } else if (mAutoDecrement) {
                decrease(cTimeTrain.getId());
                repeatUpdateHandler.postDelayed(new RepeatUpdaterTrain(), REPEAT_DELAY);
            }
        }
    }


We use RepeatUpdater class for auto-incrementing on button hold

    private class RepeatUpdaterRest implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                increase(cTimeRest.getId());
                repeatUpdateHandler.postDelayed(new RepeatUpdaterRest(), REPEAT_DELAY);
            } else if (mAutoDecrement) {
                decrease(cTimeRest.getId());
                repeatUpdateHandler.postDelayed(new RepeatUpdaterRest(), REPEAT_DELAY);
            }
        }
    }


We use RepeatUpdater class for auto-incrementing on button hold

    private class RepeatUpdaterRounds implements Runnable {
        public void run() {
            if (mAutoIncrement) {
                increase(tvRounds.getId());
                repeatUpdateHandler.postDelayed(new RepeatUpdaterRounds(), REPEAT_DELAY);
            } else if (mAutoDecrement) {
                decrease(tvRounds.getId());
                repeatUpdateHandler.postDelayed(new RepeatUpdaterRounds(), REPEAT_DELAY);
            }
        }
    }


}
*/
