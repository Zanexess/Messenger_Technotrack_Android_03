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

public class AboutFragment extends Fragment {
    private String nick;
    private String sid;
    private String cid;

    public void setInfo(String nick, String sid, String cid) {
        this.nick = nick;
        this.cid = cid;
        this.sid = sid;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, null);

        TextView nickTv = (TextView) view.findViewById(R.id.nick);
        TextView cidTv = (TextView) view.findViewById(R.id.cid);
        TextView sidTv = (TextView) view.findViewById(R.id.sid);

        nickTv.setText(nick);
        cidTv.setText(cid);
        sidTv.setText(sid);

        try {
            ((MainActivity) getActivity()).getToolbar().setTitle("Обо мне");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
