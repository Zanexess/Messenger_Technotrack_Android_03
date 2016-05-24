package com.zanexess.messenger_tech_03.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.R;

public class AboutUserFragment extends Fragment {
    private String nick;
    private String status;

    public void setInfo(String nick, String status) {
        this.nick = nick;
        this.status = status;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment_user, null);

        TextView nickTv = (TextView) view.findViewById(R.id.nick_user);
        TextView cidTv = (TextView) view.findViewById(R.id.status_user);

        nickTv.setText(nick);
        cidTv.setText(status);

        try {
            ((MainActivity) getActivity()).getToolbar().setTitle("Профиль " + nick);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
