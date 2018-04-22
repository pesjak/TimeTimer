package com.primoz.timetimer.viewholders;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.primoz.timetimer.R;
import com.primoz.timetimer.data_mvp.Workout;
import com.primoz.timetimer.extras.FontManager;
import com.primoz.timetimer.extras.Util;
import com.primoz.timetimer.interfaces.ViewHolderWorkoutListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Primož on 21/08/2017.
 */

public class ViewHolderWorkout extends RecyclerView.ViewHolder {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvTotalTime)
    TextView tvTotalTime;
    @BindView(R.id.tvRounds)
    TextView tvRounds;
    @BindView(R.id.tvWorkTime)
    TextView tvWorkTime;
    @BindView(R.id.tvRestTime)
    TextView tvRestTime;
    @BindView(R.id.btnEdit)
    Button btnEdit;


    ViewHolderWorkoutListener listener;
    Workout item;

    public ViewHolderWorkout(View itemView, final ViewHolderWorkoutListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        Typeface iconFont = FontManager.getTypeface(itemView.getContext(), FontManager.ALEGREYA_TTF);
        FontManager.markAsIconContainer(itemView.findViewById(R.id.fragment_content), iconFont);
        iconFont = FontManager.getTypeface(itemView.getContext(), FontManager.FONTAWESOME);
        btnEdit.setTypeface(iconFont);

        this.listener = listener;
    }

    @OnClick(R.id.btnEdit)
    void onClickEdit() {
        listener.onEditClicked(item);
    }

    @OnClick(R.id.cvWorkoutItem)
    void onClickItem() {
        listener.onItemClicked(item);
    }

    public void setItem(com.primoz.timetimer.data_mvp.Workout item) {
        this.item = item;

        tvName.setText(item.getName() + "");
        tvTotalTime.setText(getTime(item.getTimeTotal()));
        tvWorkTime.setText(getTime(item.getTimeWork()));
        tvRestTime.setText(getTime(item.getTimeRest()));
        tvRounds.setText(item.getTimeRounds()+"X");
    }

    private String getTime(int totalSeconds) {
        int seconds = Util.getSeconds(totalSeconds);
        int minutes = Util.getMinutes(totalSeconds);
        int hours = Util.getHours(totalSeconds);
        String timeString;
        if (hours <= 0) {
            timeString = String.format("%02d:%02d", minutes, seconds);
        } else {
            timeString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return timeString;
    }

    public com.primoz.timetimer.data_mvp.Workout getItem() {
        return item;
    }
}
