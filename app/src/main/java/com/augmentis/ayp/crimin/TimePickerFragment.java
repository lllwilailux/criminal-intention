package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * DatePickerFragment class for dace picker
 *
 * Created by Wilailux on 7/28/2016.
 */
public class TimePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {

    protected static final String EXTRA_TIME= "bbb";

    //1
    public static TimePickerFragment newInstance (Date time) {
        TimePickerFragment df = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME" , time);
        df.setArguments(args);
        return df;
    }


    TimePicker _timePicker;
    public Date time;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //3.
        time = (Date) getArguments().getSerializable("ARG_TIME");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_log_time, null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);

        _timePicker = (TimePicker) v.findViewById(R.id.time_picker_in_dialog);
        _timePicker.setHour(hour);
        _timePicker.setMinute(minute);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.time_picker_title);
        builder.setPositiveButton(android.R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        //TimePicker --> Model
        int hour =  _timePicker.getHour();
        int minute =  _timePicker.getMinute();
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.set(Calendar.HOUR,hour);
        c.set(Calendar.MINUTE,minute);

        Date date = c.getTime();
        sendResult(Activity.RESULT_OK,date);
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);


        }
}
