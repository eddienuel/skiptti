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

import com.skiptti.app.R;
import com.skiptti.app.activity.MainApplicationActivity;
import com.skiptti.app.control.ScrollSelectorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emmanuel on 24/02/2017.
 *
 */

public class SelectTargetDialogFragment extends DialogFragment {

    View view;
    Button ok;
    ScrollSelectorView scroll;
    int targetValue;

    public SelectTargetDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.select_skip_target_view, container);
        ok = (Button) view.findViewById(R.id.ok);
        scroll = (ScrollSelectorView) view.findViewById(R.id.target_selector);
        List<String> data = new ArrayList<>();
        for (int i = 0; i < 50; i++)
        {
            data.add(String.valueOf((i*10)));
        }

        scroll.setData(data);
        targetValue = scroll.getSelected();
        scroll.setOnSelectListener(new ScrollSelectorView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                targetValue = Integer.valueOf(text);
            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle  = new Bundle();
                bundle.putInt("target", targetValue);
                ((MainApplicationActivity)getActivity()).switchFragment(new TargetSkipFragment(), bundle, 3);
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
        Window window = getDialog().getWindow();
        Point size = new Point();
        if (window != null) {
            Display display = window.getWindowManager().getDefaultDisplay();
            display.getSize(size);
            window.setLayout((int) (size.x * 1.0), (int) (size.y * 1.0));
            window.setGravity(Gravity.CENTER);
        }
        super.onResume();
    }
}
