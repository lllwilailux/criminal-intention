package com.augmentis.ayp.crimin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Wilailux on 7/18/2016.
 */
public class CrimeLab {

    List<Crime> crimeList;

    private static CrimeLab instance;

    //เป็น singleton
    public static CrimeLab getInstance() {
        if ( instance == null) {
            instance = new CrimeLab();
        }
        return instance;
    }

    private CrimeLab() {
        crimeList = new ArrayList<>();

        for (int i = 1; i <= 100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #" + i);
            crime.setSolved( i % 2 == 0);

            crimeList.add(crime);
        }
    }

    public Crime getCrimeById(UUID uuid){
        for (Crime crime : crimeList) {
            if (crime.getId().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }

    public List<Crime> getCrimes() {
        return crimeList;
    }

    public static void main (String[] args) {
        CrimeLab crimeLab = CrimeLab.getInstance();
        List<Crime> crimeList = crimeLab.getCrimes();
        int size = crimeList.size();
        for( int i=0; i<size; i++){
            System.out.println(crimeList.get(i));
        }
        System.out.println(crimeList.toString());

        System.out.println(CrimeLab.getInstance());

    }

}
