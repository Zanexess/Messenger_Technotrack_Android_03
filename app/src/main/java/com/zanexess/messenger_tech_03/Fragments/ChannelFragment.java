package com.zanexess.messenger_tech_03.Fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.Messages.LeaveData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.Messages.MessageData;
import com.zanexess.messenger_tech_03.Messages.UserInfoData;
import com.zanexess.messenger_tech_03.Objects.Channel;
import com.zanexess.messenger_tech_03.Objects.LastMsg;
import com.zanexess.messenger_tech_03.Objects.User;
import com.zanexess.messenger_tech_03.R;

import java.util.ArrayList;
import java.util.List;

public class ChannelFragment extends Fragment implements View.OnClickListener {
    private final String LOG_TAG = getClass().getSimpleName();
    private ListView msgListView;
    private EditText msg_edittext;
    private List<LastMsg> chatlist;
    private static ChatAdapter chatAdapter;
    private View view;
    private Channel channel;

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.channel_fragment, null);
        msg_edittext = (EditText) view.findViewById(R.id.messageEditText);
        msgListView = (ListView) view.findViewById(R.id.msgListView);
        ImageButton sendButton = (ImageButton) view
                .findViewById(R.id.sendMessageButton);
        sendButton.setOnClickListener(this);

        msgListView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        msgListView.setStackFromBottom(true);

        chatlist = new ArrayList<>();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    chatAdapter = new ChatAdapter(getActivity(), (List<LastMsg>)((MainActivity) getActivity()).getMessengerService().getMessages());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                msgListView.setAdapter(chatAdapter);
            }
        }, 400);

        chatAdapter = new ChatAdapter(getActivity(), chatlist);
        msgListView.setAdapter(chatAdapter);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendMessageButton:
                if (!msg_edittext.getText().toString().equals("")) {
                    try {
                        Message message = new Message("message",
                                new MessageData(((MainActivity) getActivity()).getMessengerService().getCid(),
                                        ((MainActivity) getActivity()).getMessengerService().getSid(), channel.getChid(), msg_edittext.getText().toString()));
                        Gson gson = new Gson();
                        String msg = gson.toJson(message);
                        ((MainActivity)getActivity()).getMessengerService().sendMessage(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    msg_edittext.setText("");
                }
        }
    }

    public class ChatAdapter extends BaseAdapter {

        private LayoutInflater inflater = null;
        ArrayList<LastMsg> chatMessageList;

        public void setChatMessageList(ArrayList<LastMsg> chatMessageList) {
            this.chatMessageList = chatMessageList;
        }

        public ChatAdapter(Activity activity, List<LastMsg> list) {
            chatMessageList = (ArrayList<LastMsg>) list;
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return chatMessageList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LastMsg message = chatMessageList.get(position);
            View vi = convertView;
            if (convertView == null)
                vi = inflater.inflate(R.layout.chatbubble, null);

            TextView msg = (TextView) vi.findViewById(R.id.message_text);
            TextView num_message = (TextView) vi.findViewById(R.id.num_message);
            TextView time = (TextView) vi.findViewById(R.id.time);


            LinearLayout layout = (LinearLayout) vi
                    .findViewById(R.id.bubble_layout);
            LinearLayout parent_layout = (LinearLayout) vi
                    .findViewById(R.id.bubble_layout_parent);

            ImageView imageView = (ImageView) vi.findViewById(R.id.image1);
            try {
                if (message.getFrom().equals("-1")) {
                    layout.setBackgroundResource(R.drawable.bubble3);
                    msg.setText(message.getBody());
                    imageView.setVisibility(View.GONE);
                    parent_layout.setGravity(Gravity.CENTER_HORIZONTAL);
                    time.setText("");
                    num_message.setText("");
                } else if (message.getFrom().equals(((MainActivity)getActivity()).getMessengerService().getCid())) {
                    layout.setBackgroundResource(R.drawable.bubble2);
                    msg.setText(message.getBody());
                    imageView.setVisibility(View.GONE);
                    num_message.setText("Сообщение №" + (position + 1));
                    parent_layout.setGravity(Gravity.RIGHT);
                    time.setText(message.getTime());
                } else {
                    layout.setBackgroundResource(R.drawable.bubble1);
                    imageView.setVisibility(View.VISIBLE);
                    msg.setText(message.getBody());
                    num_message.setText("Сообщение №" + (position + 1));
                    parent_layout.setGravity(Gravity.LEFT);
                    time.setText(message.getTime());
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            msg.setTextColor(Color.BLACK);

            if (imageView.getVisibility() == View.VISIBLE) {
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            //Скорее заглушка на будущее
                            List<User> users = (List<User>)((MainActivity)getActivity()).getMessengerService().getUsers();
                            User userFound = null;
                            for (User user : users) {
                                if (message.getFrom().equals(user.getUid())) {
                                    userFound = user;
                                    Toast.makeText(getActivity().getApplicationContext(), message.getFrom() + " in this chat", Toast.LENGTH_SHORT).show();
                                    break;
                                }
                            }
                            Message messageTo = new Message("userinfo", new UserInfoData(message.getFrom(),
                                    ((MainActivity)getActivity()).getMessengerService().getCid(),
                                    ((MainActivity)getActivity()).getMessengerService().getSid()));
                            Gson gson = new Gson();
                            String msg = gson.toJson(messageTo);
                            ((MainActivity)getActivity()).getMessengerService().sendMessage(msg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }

            return vi;
        }

        public void add(LastMsg object) {
            chatMessageList.add(object);
        }
    }

    @Override
    public void onDestroy() {
        leaveChannel();
        super.onDestroy();
    }

    private void leaveChannel() {
        Log.v(LOG_TAG, "LEAVE");
        try {
            Message message = new Message("leave", new LeaveData(
                    ((MainActivity) getActivity()).getMessengerService().getCid(),
                    ((MainActivity) getActivity()).getMessengerService().getSid(), channel.getChid()));
            Gson gson = new Gson();
            String msg = gson.toJson(message);
            ((MainActivity)getActivity()).getMessengerService().sendMessage(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void refresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    chatlist = (List<LastMsg>)((MainActivity) getActivity()).getMessengerService().getMessages();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                chatAdapter.setChatMessageList((ArrayList<LastMsg>)chatlist);
                chatAdapter.notifyDataSetInvalidated();
                msgListView.deferNotifyDataSetChanged();
                msgListView.invalidateViews();
            }
        });
    }
}
