package com.augmentis.ayp.crimin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

//import java.text.DateFormat;

/**
 * Created by Wilailux on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String CRIME_POSITION = "CrimeFragment.CRIME_POSITION";
    private static final String DIALOG_DATE = "CrimeDateDialogFragment";
    private static final int REQUEST_DATE = 222;
    private static final int REQUEST_TIME = 111;
    private static final int REQUEST_CONTACT_SUSPECT = 333;
    private static final String DIALOG_TIME = "CrimeTimeDialogFragment";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 444 ;
    private static final String TAG = "CrimeFragment";
    private static final int REQUEST_CAPTURE_PHOTO = 555;
    private static final int REQUEST_PIC = 666;
    private static final String DIALOG_PIC = "CrimePicDialogFragment";


    private Crime crime;
    private File photoFile;

    private EditText editText;
    private Button crimeDateButton;
    private Button crimeTimeButton;
    private Button crimeDeleteButton;
    private CheckBox crimeSolvedCheckbox;
    private Button crimeReportButton;
    private Button crimeSuspectButton;
    private Button crimeCallSuspectButton;
    private ImageView photoView;
    private ImageView photoListView;
    private ImageButton photoButton;
    private Callbacks callbacks;

    public CrimeFragment() {}

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);

        /**
         *  สร้างตัวมันเองขึ้นมา แล้ว set arg ให้ แล้วส่งกลับไปให้ crime fragment
         */
        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }


    // callback
    public interface Callbacks {
        void onCrimeUpdated(Crime crime);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null ;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());


        if (getArguments().get(CRIME_ID) != null) {
            UUID crimeId =(UUID) getArguments().getSerializable(CRIME_ID);
            crime = crimeLab.getCrimeById(crimeId);
        } else {
            // == null
            Crime crime = new Crime();
            CrimeLab.getInstance(getActivity()).addCrime(crime);
            this.crime = crime;
        }
        Log.d(CrimeListFragment.TAG, "crime.getTitle() =" + crime.getTitle());

        photoFile = CrimeLab.getInstance(getActivity()).getPhotoFile(crime);

        setHasOptionsMenu(true);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
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
                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        crimeDateButton = (Button) v.findViewById(R.id.crime_date);
        crimeDateButton.setText(getFormattedDate(crime.getCrimeDate()));
        crimeDateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                DatePickerFragment dialogFragment =
                        DatePickerFragment.newInstance(crime.getCrimeDate());
                dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                dialogFragment.show(fm, DIALOG_DATE);

            }
        });


        crimeTimeButton = (Button) v.findViewById(R.id.crime_time);
        crimeTimeButton.setText(getFormattedTime(crime.getCrimeDate()));
        crimeTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                TimePickerFragment dialogFragment =
                        TimePickerFragment.newInstance(crime.getCrimeDate());
                dialogFragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                dialogFragment.show(fm, DIALOG_TIME);
            }
        });


//        crimeDeleteButton = (Button) v.findViewById(R.id.crime_delete);
//        crimeDeleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                CrimeLab.getInstance(getActivity()).deleteCrime(crime.getId());
//                getActivity().finish();
//            }
//        });


        crimeSolvedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSolvedCheckbox.setChecked(crime.isSolved());
        crimeSolvedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                    updateCrime();
                //Log.d(CrimeActivity.TAG, "Crime:" + crime.toString());
            }
        });


        /**
         * Implicit Intent
         */
        crimeReportButton = (Button) v.findViewById(R.id.crime_report);
        crimeReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); // MIME Type
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject));

                // หน้าที่ขึ้นให้เลือก app ที่จะส่ง เช่น line,facebook
                i = Intent.createChooser(i, getString(R.string.send_report));

                startActivity(i);
            }
        });

        // ถ้าไม่ประกาศ final จะไม่สามารถเอาไปใช้ใน class onclick ได้
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);// content provider
       // pickContact.addCategory(Intent.CATEGORY_HOME);

        crimeSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        crimeSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(pickContact, REQUEST_CONTACT_SUSPECT);
            }
        });

        if (crime.getSuspect() != null) {
            crimeSuspectButton.setText(crime.getSuspect());
        }


        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null) {
            crimeSuspectButton.setEnabled(false);
        }


        crimeCallSuspectButton = (Button) v.findViewById(R.id.call_crime_suspect);
        crimeCallSuspectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCallPermission()) {
                    callSuspect();
                }

            }
        });


        photoButton = (ImageButton) v.findViewById(R.id.crime_camera);
        photoView = (ImageView) v.findViewById(R.id.crime_photo);


        // Call camera intent
        final Intent captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // check if we can take photo
        boolean canTakePhoto = photoFile != null && captureImageIntent.resolveActivity(packageManager) != null ;

        if (canTakePhoto) {
            Uri uri = Uri.fromFile(photoFile);
            Log.d(TAG,"File output at " + photoFile.getAbsolutePath());
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }

        // on click -> start activity for camera
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(captureImageIntent,REQUEST_CAPTURE_PHOTO);
            }
        });


        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fm = getFragmentManager();
                DialogPicFragment dialogPicFragment = DialogPicFragment.newInstance(photoFile);
                dialogPicFragment.setTargetFragment(CrimeFragment.this, REQUEST_PIC);
                dialogPicFragment.show(fm, DIALOG_PIC);

            }
        });





        // Update photo changing
        updatePhotoView();
        return v;
    }



    private String getFormattedDate(Date date){
        return new SimpleDateFormat("d MMMM yyyy").format(date);

    }

    private String getFormattedTime(Date date){
        return new SimpleDateFormat("HH:mm a").format(date);
    }

    @Override
    public void onActivityResult(int requestCode, int result, Intent data) {
        if (result != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE) {
           Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            // set
            crime.setCrimeDate(date);
            updateCrime();

            crimeDateButton.setText(getFormattedDate(crime.getCrimeDate()));
        }

        if (requestCode == REQUEST_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);

            // set
            crime.setCrimeDate(date);
            updateCrime();
            crimeTimeButton.setText(getFormattedTime(crime.getCrimeDate()));
        }

        if (requestCode == REQUEST_CONTACT_SUSPECT) {
            if (data != null) {
                Uri contactUri = data.getData();
                String[] queryFields = new String[] {
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER };
                Cursor c = getActivity()
                        .getContentResolver()
                        .query(contactUri,
                                queryFields,
                                null,
                                null,
                                null);
                try {
                    if (c.getCount() == 0) {
                        return;
                    }

                    c.moveToFirst();
                    String suspect = c.getString(0);// ชื่อ DISPLAY_NAME
                    suspect = suspect + ": " + c.getString(1);// เบอร์ NUMBER

                    crime.setSuspect(suspect);
                    updateCrime();
                    crimeSuspectButton.setText(suspect);
                } finally {
                    c.close();
                }
            }

          }

       if (requestCode == REQUEST_CAPTURE_PHOTO) {
            updatePhotoView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        updateCrime();
    }

    public void updateCrime(){
        CrimeLab.getInstance(getActivity()).updateCrime(crime);// update crime in database after click back
        callbacks.onCrimeUpdated(crime);
    }

    public void callSuspect() {

        Intent i = new Intent(Intent.ACTION_CALL);
        StringTokenizer tokenizer = new StringTokenizer(crime.getSuspect(), ":");
        String name = tokenizer.nextToken();
        String phone = tokenizer.nextToken();
        Log.d(TAG, "calling " + name + "/" + phone);
        i.setData(Uri.parse("tel:" + phone));

        startActivity(i);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callSuspect();
                } else {
                    Toast.makeText(getActivity(),R.string.denied_permission_to_call,Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    private boolean hasCallPermission() {

        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

            return false; // checking -- wait for dialog
        }

        return true; // already has permission
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.crime_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(crime.getId());
                getActivity().finish();
        default:
        return super.onOptionsItemSelected(item);}
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (crime.isSolved()) {
            solvedString = getString(R.string.crime_report_solved);
        } else {
            solvedString = getString(R.string.crime_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, crime.getCrimeDate()).toString();

        String suspect = crime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_with_suspect, suspect);
        }
        String report = getString(R.string.crime_report,crime.getTitle(),dateString,solvedString,suspect);
        return report;
    }

    private void updatePhotoView() {
        if(photoFile == null || !photoFile.exists()) {
            photoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(photoFile.getPath(), getActivity());
            photoView.setImageBitmap(bitmap);
        }
    }

}
