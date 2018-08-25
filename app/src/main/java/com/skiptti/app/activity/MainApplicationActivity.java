package com.skiptti.app.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.skiptti.app.fragments.BaseFragment;
import com.skiptti.app.fragments.SplashScreenFragment;
import com.skiptti.app.R;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainApplicationActivity extends AppCompatActivity{

    private BaseFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_application);
        BaseFragment splashScreenFragment = new SplashScreenFragment();
        switchFragment(splashScreenFragment, null, 1);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @SuppressLint("PrivateResource")
    public void switchFragment(BaseFragment fragment, Bundle bundle, int id) {
        this.fragment = fragment;
        if (bundle == null) {
            bundle = new Bundle();
            bundle.putInt("ID", id);
        } else {
            bundle.putInt("ID", id);
        }
        this.fragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        fragmentTransaction.replace(R.id.fragment_parent_group, this.fragment, fragment.getClass().getSimpleName());
        fragmentTransaction.disallowAddToBackStack();
        fragmentTransaction.commitAllowingStateLoss();
    }
}


//TFzi8NgLM4WhJzfi8Y9T
//http://www.truiton.com/2017/01/android-bottom-navigation-bar-example/