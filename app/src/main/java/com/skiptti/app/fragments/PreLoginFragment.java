package com.skiptti.app.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.github.florent37.viewanimator.AnimationBuilder;

import org.sufficientlysecure.htmltextview.HtmlTextView;
import com.skiptti.app.R;
import com.skiptti.app.modules.skip.target.SelectTargetDialogFragment;

/**
 * @author emmanuel edwards
 */
public class PreLoginFragment extends BaseFragment {

    View view;
    AnimationBuilder builder;
    public PreLoginFragment() {
        // Required empty public constructor
        //This is a test comment
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_pre_login, container, false);

        HtmlTextView htmlTextView = (HtmlTextView) view.findViewById(R.id.pre_login_message);
        htmlTextView.setHtml(getString(R.string.text_pre_login_message));

        Button start = (Button)view.findViewById(R.id.star_skip);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectTargetDialogFragment dialogFragment = new SelectTargetDialogFragment();
                FragmentManager manager = activity.getSupportFragmentManager();
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.Dialog_FullScreen);
                dialogFragment.show(manager, "target");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //builder.start();
    }
}
