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
import android.widget.Toast;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.Messages.CreateChannelData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.R;

public class CreateChatFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_chat_fragment, null);

        final EditText chatNameEditText = (EditText) view.findViewById(R.id.chat_name);
        final EditText chatDescrEditText = (EditText) view.findViewById(R.id.chat_descr);
        Button chatCreate = (Button) view.findViewById(R.id.chat_create);

        chatCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!chatDescrEditText.getText().toString().equals("") || !chatNameEditText.getText().toString().equals("")) {
                    try {
                        Message message = new Message("createchannel", new CreateChannelData(
                                ((MainActivity) getActivity()).getMessengerService().getCid(),
                                ((MainActivity) getActivity()).getMessengerService().getSid(),
                                chatNameEditText.getText().toString(),
                                chatDescrEditText.getText().toString()));
                        Gson gson = new Gson();
                        String msg = gson.toJson(message);
                        ((MainActivity)getActivity()).getMessengerService().sendMessage(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Некорректные данные", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ((MainActivity)getActivity()).getToolbar().setTitle("Создать чат");
        return view;
    }
}
