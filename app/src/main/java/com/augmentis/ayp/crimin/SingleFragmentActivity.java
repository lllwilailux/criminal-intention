package com.augmentis.ayp.crimin;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected static final String TAG = "augment-ayp";

    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        Log.d(CrimeListFragment.TAG, "On Create activity");

        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentById(R.id.fragment_container);

        if( f == null) {
            f = onCreateFragment();

            /**
             * create fragment anf commit to fragment container
             */
            fm.beginTransaction()
                    .add(R.id.fragment_container, f)
                    .commit();
            Log.d(CrimeListFragment.TAG, "Fragment is create");
        } else {
            Log.d(CrimeListFragment.TAG, "Fragment have already been create");
        }
    }

    protected abstract Fragment onCreateFragment();
}
