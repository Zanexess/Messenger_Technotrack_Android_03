package com.zanexess.messenger_tech_03.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.MessageSocketService;
import com.zanexess.messenger_tech_03.R;

public class SplashFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().startService(new Intent(getActivity(), MessageSocketService.class));

        return inflater.inflate(R.layout.splash_screen_fragment, null);
    }

    @Override
    public void onStart() {
        introActivity(2000);
        super.onStart();
    }

    private void introActivity(final Integer num) {
        Thread timer = new Thread()
        {
            public void run()
            {
                try
                {
                    int logoTimer = 0;
                    while(logoTimer < num) {
                        sleep(100);
                        logoTimer = logoTimer +100;
                    };
                    try {
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        LoginFragment loginFragment = new LoginFragment();
                        ((MainActivity)getActivity()).setLoginFragment(loginFragment);
                        ft.replace(R.id.main_frame_layout, loginFragment);
                        ft.commit();
                    } catch (Exception e) {

                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

                }
            }
        };
        timer.start();
    }
}
