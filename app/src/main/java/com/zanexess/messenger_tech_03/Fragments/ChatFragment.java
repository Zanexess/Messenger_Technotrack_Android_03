package com.zanexess.messenger_tech_03.Fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.MainActivity;
import com.zanexess.messenger_tech_03.Messages.ChannelListData;
import com.zanexess.messenger_tech_03.Messages.EnterData;
import com.zanexess.messenger_tech_03.Messages.Message;
import com.zanexess.messenger_tech_03.Objects.Channel;
import com.zanexess.messenger_tech_03.R;

import java.util.List;


public class ChatFragment extends Fragment {
    private ChannelFragment channelFragment = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, null);

        final RecyclerView rv = (RecyclerView) view.findViewById(R.id.recyclerView);
        rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        rv.setLayoutManager(llm);
        List<Channel> channels = null;
        try {
            channels = (List<Channel>)((MainActivity) getActivity()).getMessengerService().getChannels();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        RVAdapter adapter = new RVAdapter(channels);
        rv.setAdapter(adapter);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipereflesh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    Message message = new Message("channellist", new ChannelListData(
                        ((MainActivity)getActivity()).getMessengerService().getCid(),
                        ((MainActivity)getActivity()).getMessengerService().getSid()));
                    Gson gson = new Gson();
                    String msg = gson.toJson(message);
                    ((MainActivity) getActivity()).getMessengerService().sendMessage(msg);
                } catch ( RemoteException e){

                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        RVAdapter adapter = null;
                        try {
                            adapter = new RVAdapter((List<Channel>)((MainActivity)getActivity()).getMessengerService().getChannels());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        rv.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });

        ((MainActivity)getActivity()).getToolbar().setTitle("Список чатов");

        return view;
    }

    public void refreshChannel() {
        channelFragment.refresh();
    }

    public class RVAdapter extends RecyclerView.Adapter<RVAdapter.ChatViewHolder> {
        private List<Channel> channels;

        public RVAdapter(List<Channel> channels) {
            this.channels = channels;
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            ChatViewHolder chatViewHolder = new ChatViewHolder(v);
            return chatViewHolder;
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {
            final Channel channel = channels.get(position);
            String info = channel.getName() + "(" + channel.getOnline() + ")";
            holder.title.setText(info);
            holder.description.setText(channel.getDescr());
            holder.cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Message message = new Message("enter", new EnterData(
                                ((MainActivity) getActivity()).getMessengerService().getCid(),
                                ((MainActivity) getActivity()).getMessengerService().getSid(), channel.getChid()));
                        Gson gson = new Gson();
                        String msg = gson.toJson(message);
                        ((MainActivity) getActivity()).getMessengerService().sendMessage(msg);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    channelFragment = new ChannelFragment();
                    try {
                        ((MainActivity)getActivity()).getMessengerService().setChannelId(channel.getChid());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    channelFragment.setChannel(channel);
                    ft.replace(R.id.main_frame_layout, channelFragment);
                    ft.addToBackStack("channel");
                    ft.commit();
                    ((MainActivity)getActivity()).setTitle(channel.getName());
                }
            });
        }

        @Override
        public int getItemCount() {
            if (channels != null) {
                return channels.size();
            } else {
                Toast.makeText(getActivity(), "Что-то пошло не так, пожалуйста, обновите данные", Toast.LENGTH_SHORT).show();
                return 0;
            }
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder {
            CardView cv;
            TextView title;
            TextView description;
            ImageView chatImage;

            ChatViewHolder(View itemView) {
                super(itemView);
                cv = (CardView) itemView.findViewById(R.id.cv);
                title = (TextView) itemView.findViewById(R.id.title);
                description = (TextView) itemView.findViewById(R.id.description);
                chatImage = (ImageView) itemView.findViewById(R.id.chatImage);
            }
        }
    }
}
