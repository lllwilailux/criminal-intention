package com.augmentis.ayp.crimin;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity  implements CrimeListFragment.Callbacks , CrimeFragment.Callbacks{

    /**
     * ต้องการสร้าง fragment เลยต้องไปเเรียก onCreateFragment ใน Crime ListActivity
     *
     */
    @Override
    protected Fragment onCreateFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            // single pane
            Intent intent = CrimePagerActivity.newIntent(this,crime.getId());
            startActivity(intent);
        } else {

            Log.d(TAG,"On crime selected :" + crime);




                Fragment newDetailFragment = CrimeFragment.newInstance(crime.getId());

                // replace old fragment with new one
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetailFragment)
                        .commit();
        }

    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        // Update List
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        listFragment.updateUI();
    }

    @Override
    public void onCrimeDelete() {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);

        CrimeFragment detailFragment = (CrimeFragment) getSupportFragmentManager().findFragmentById(R.id.detail_fragment_container );

        listFragment.updateUI();

        //clear
        getSupportFragmentManager().beginTransaction().detach(detailFragment).commit();
    }

}
