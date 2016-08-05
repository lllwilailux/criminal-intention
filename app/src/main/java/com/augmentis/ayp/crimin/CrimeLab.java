package com.augmentis.ayp.crimin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.augmentis.ayp.crimin.CrimeDbSchema.CrimeTable;

/**
 * Created by Wilailux on 7/18/2016.
 */
public class CrimeLab {

    //////////////////////////////// STATIC //////////////////////////////////////////
    // instance เป็น static มันจะผูกติดกับ class CrimeLab
    private static CrimeLab instance;
    private static final String TAG = "CrimeLab";

    //เป็น singleton
    //สร้าง CrimeLab ขึ้นมา
    public static CrimeLab getInstance(Context context) {
        if ( instance == null) {
            instance = new CrimeLab(context);
        }
        return instance;
    }

    /**
     * คล้ายๆกับ Buldle ใน Intent
     */
    public static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getCrimeDate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, (crime.isSolved()) ? 1:0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    /////////////////////////////////////////////////////////////////////////////////

    private Context context;
    private SQLiteDatabase database;

    private CrimeLab(Context context) {

        this.context = context.getApplicationContext();
        CrimesBaseHelper crimesBaseHelper = new CrimesBaseHelper(context);
        database = crimesBaseHelper.getWritableDatabase();
    }

    public Crime getCrimeById(UUID uuid){
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ?", new String[]{uuid.toString()} );

        try {
            if(cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }


    public CrimeCursorWrapper queryCrimes (String whereCause, String[] whereArgs) {
        Cursor cursor = database.query(CrimeTable.NAME,
                null,
                whereCause,
                whereArgs,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrimes() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        // isAfterLast หลังจากบันทัดสุดท้าย
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public void addCrime(Crime crime){
        Log.d(TAG, "ADD crime" + crime.toString());
        ContentValues contentValues = getContentValues(crime);

        database.insert(CrimeTable.NAME, null, contentValues);
    }

    public void deleteCrime(UUID uuid){
        database.delete(CrimeTable.NAME, CrimeTable.Cols.UUID + " = ?", new String[] {uuid.toString()});
    }

    public void updateCrime(Crime crime) {
        Log.d(TAG, "Update crime" + crime.toString());
        String uuidStr = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        database.update(CrimeTable.NAME, contentValues, CrimeTable.Cols.UUID + " = ?", new String[] {uuidStr});
    }


    public File getPhotoFile(Crime crime) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }
        return  new File(externalFilesDir, crime.getPhotoFilename());
    }

}
