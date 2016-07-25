package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Wilailux on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String CRIME_POSITION = "CrimeFragment.CRIME_POSITION";

    private Crime crime;
    private int position;
    private EditText editText;

    private Button crimeDateButton;
    private CheckBox crimeSolvedCheckbox;

    public CrimeFragment() {}

    public static CrimeFragment newInstance(UUID crimeId, int position) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);
        args.putInt(CRIME_POSITION, position);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeID = (UUID) getArguments().getSerializable(CRIME_ID);
        position = getArguments().getInt(CRIME_POSITION);
        crime = CrimeLab.getInstance().getCrimeById(crimeID);
        Log.d(CrimeListFragment.TAG, "crime.getId() =" + crime.getId());
        Log.d(CrimeListFragment.TAG, "crime.getTitle() =" + crime.getTitle());
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        editText = (EditText) v.findViewById(R.id.crime_title);
        editText.setText(crime.getTitle());
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        crimeDateButton = (Button) v.findViewById(R.id.crime_date);
       // crimeDateButton.setText(new SimpleDateFormat( "d MMMM yyyy" ).format(crime.getCrimeDate())); set format date เหมือน method getFormatteDate ข้างล่าง
        crimeDateButton.setText(getFormattedDate(crime.getCrimeDate()));
        crimeDateButton.setEnabled(false);

        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckbox.setChecked(crime.isSolved());
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeActivity.TAG, "Crime:" + crime.toString());
            }
        });

        Intent intent = new Intent();
        intent.putExtra("position", position);
        Log.d(CrimeListFragment.TAG, "send position back : " + position);
        getActivity().setResult(Activity.RESULT_OK, intent);

        return v;
    }


    private String getFormattedDate(Date date){
        return new SimpleDateFormat("d MMMM yyyy").format(date);
    }

}
