package com.augmentis.ayp.crimin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity {

    /**
     * ต้องการสร้าง fragment เลยต้องไปเเรียก onCreateFragment ใน Crime ListActivity
     *
     */
    @Override
    protected Fragment onCreateFragment() {
        return new CrimeListFragment();
    }
}
