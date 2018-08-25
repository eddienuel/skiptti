package com.skiptti.app.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
import com.skiptti.app.R;

import org.sufficientlysecure.htmltextview.HtmlTextView;

/**
 * @author emmanuel edwards
 */
public class SplashScreenFragment extends BaseFragment {

    View view;
    public SplashScreenFragment() {
        // Required empty public constructor
        //This is a test comment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_splash_screen, container, false);
        HtmlTextView htmlTextView = (HtmlTextView) view.findViewById(R.id.splash_message);
        htmlTextView.setHtml(getString(R.string.text_pre_login_message));
        ViewAnimator
                .animate(view)
                .scale(1,1).duration(1000)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        activity.switchFragment(new PreLoginFragment(), null, 1);
                    }
                })
                .start();
        return view;
    }


}
