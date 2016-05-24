package com.zanexess.messenger_tech_03.Fragments;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.Messages.SetUserInfoData;
import com.zanexess.messenger_tech_03.R;

public class ChangeAboutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.change_about_fragment, null);

        final EditText status = (EditText) view.findViewById(R.id.aboutStatusEditText);
        Button aboutChange = (Button) view.findViewById(R.id.aboutButtonChange);

        aboutChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!status.getText().equals("")) {
                    try {
                        Message message = new Message("setuserinfo",
                                new SetUserInfoData(status.getText().toString(),
                                        ((MainActivity) getActivity()).getMessengerService().getCid(),
                                        ((MainActivity) getActivity()).getMessengerService().getSid()));
                        Gson gson = new Gson();
                        String msg = gson.toJson(message);
                        ((MainActivity) getActivity()).getMessengerService().sendMessage(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        ((MainActivity)getActivity()).getToolbar().setTitle("Изменить информацию");
        return view;
    }
}
