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
import com.zanexess.messenger_tech_03.Messages.RegistrationData;
import com.zanexess.messenger_tech_03.R;

public class RegistrationFragment extends Fragment {
    private Button register;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.registration_fragment, null);

        final EditText email = (EditText) view.findViewById(R.id.email);
        final EditText password = (EditText) view.findViewById(R.id.password);
        final EditText nick = (EditText) view.findViewById(R.id.nickname);

        register = (Button) view.findViewById(R.id.register);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message("register", new RegistrationData(email.getText().toString(),
                        password.getText().toString(), nick.getText().toString()));
                Gson gson = new Gson();
                String msg = gson.toJson(message);
                try {
                    ((MainActivity)getActivity()).getMessengerService().sendMessage(msg);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }

}
