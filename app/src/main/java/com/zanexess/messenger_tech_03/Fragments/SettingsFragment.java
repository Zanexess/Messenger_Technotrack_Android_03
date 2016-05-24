package com.zanexess.messenger_tech_03.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.R;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, null);
        ((MainActivity)getActivity()).getToolbar().setTitle("Настройки");
        return view;
    }
}
