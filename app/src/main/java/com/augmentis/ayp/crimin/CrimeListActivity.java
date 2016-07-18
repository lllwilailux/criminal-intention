package com.augmentis.ayp.crimin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment onCreateFragment() {
        return new CrimeListFragment();
    }
}
