package com.zanexess.messenger_tech_03.Fragments;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.Messages.LoginData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.R;

public class LoginFragment extends Fragment {
    private Button login;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, null);

        final EditText loginEditView = (EditText) view.findViewById(R.id.emailEditView);
        final EditText passEditView = (EditText) view.findViewById(R.id.passwordEditView);

        login = (Button) view.findViewById(R.id.login);
        Button register = (Button) view.findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main_frame_layout, new RegistrationFragment());
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message("auth", new LoginData(loginEditView.getText().toString(),
                        passEditView.getText().toString()));
                Gson gson = new Gson();
                String msg = gson.toJson(message);
                try {
                    ((MainActivity)getActivity()).getMessengerService().sendMessage(msg);
                    ((MainActivity)getActivity()).saveText(loginEditView.getText().toString(),
                            passEditView.getText().toString());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }

    public void isLogin() {
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        AboutFragment aboutFragment = new AboutFragment();
        try {
            aboutFragment.setInfo(((MainActivity) getActivity()).getMessengerService().getNick(),
                    ((MainActivity) getActivity()).getMessengerService().getSid(),
                    ((MainActivity) getActivity()).getMessengerService().getCid());
            ((MainActivity)getActivity()).checkChannels();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ft.replace(R.id.main_frame_layout, aboutFragment);
        ft.commit();
    }
}
