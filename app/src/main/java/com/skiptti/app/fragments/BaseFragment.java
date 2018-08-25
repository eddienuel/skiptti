package com.skiptti.app.fragments;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.skiptti.app.activity.MainApplicationActivity;

/**
 * Created by emmanuel on 05/07/2017.
 *
 */

public class BaseFragment extends Fragment{

    public MainApplicationActivity activity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainApplicationActivity)getActivity();
    }
}
