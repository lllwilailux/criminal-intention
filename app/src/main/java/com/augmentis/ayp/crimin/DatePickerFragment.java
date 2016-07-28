package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * DatePickerFragment class for dace picker
 *
 * Created by Wilailux on 7/28/2016.
 */
public class DatePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {

    protected static final String EXTRA_DATE = "aaa";

    //1
    public static DatePickerFragment newInstance (Date date) {
        DatePickerFragment df = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_DATE" , date);
        df.setArguments(args);
        return df;
    }


    DatePicker _datePicker;
    public Date date;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //3.
        date = (Date) getArguments().getSerializable("ARG_DATE");

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_log, null);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        _datePicker = (DatePicker) v.findViewById(R.id.date_picker_in_dialog);
        _datePicker.init(year, month, dayOfMonth,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.date_picker_title);
        builder.setPositiveButton(android.R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {
        //DatePicker --> Model
        int dayOfMonth =  _datePicker.getDayOfMonth();
        int month =  _datePicker.getMonth();
        int year =  _datePicker.getYear();

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.YEAR,year);

        Date date = c.getTime();

        //Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
        sendResult(Activity.RESULT_OK,date);
    }

    private void sendResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);


        }
}
