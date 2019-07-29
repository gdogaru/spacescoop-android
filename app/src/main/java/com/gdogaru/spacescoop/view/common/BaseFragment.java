package com.gdogaru.spacescoop.view.common;

import androidx.fragment.app.Fragment;

import com.gdogaru.spacescoop.R;
import com.gdogaru.spacescoop.di.Injectable;
import com.squareup.otto.Bus;

import javax.inject.Inject;

public class BaseFragment extends Fragment implements Injectable {

    @Inject
    protected Bus bus;

    public boolean isLandscapeTablet() {
        return getResources().getBoolean(R.bool.isLandscapeTablet);
    }

    @Override
    public void onResume() {
        super.onResume();
        bus.register(this);
    }

    @Override
    public void onPause() {
        bus.unregister(this);
        super.onPause();
    }
}
