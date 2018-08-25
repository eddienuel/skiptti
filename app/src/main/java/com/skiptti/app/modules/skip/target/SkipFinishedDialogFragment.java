package com.skiptti.app.modules.skip.target;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.skiptti.app.R;
import com.skiptti.app.activity.MainApplicationActivity;
import com.skiptti.app.control.ScrollSelectorView;
import com.skiptti.app.fragments.PreLoginFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 24/02/2017.
 *
 */

public class SkipFinishedDialogFragment extends DialogFragment {

    View view;
    Button ok, share;

    public SkipFinishedDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_finished_skip_view, container);
        ok = (Button) view.findViewById(R.id.ok);
        share = (Button) view.findViewById(R.id.share);
        TextView messageStatus = (TextView) view.findViewById(R.id.sub_title);

        String message = getArguments().getString("message");
        messageStatus.setText(message);

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Oh! nice, sharing is good but its currently unavailable.", Toast.LENGTH_LONG).show();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainApplicationActivity)getActivity()).switchFragment(new PreLoginFragment(), null, 3);
                dismiss();
            }
        });
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onResume() {

        super.onResume();
    }
}
