package com.zanexess.messenger_tech_03;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zanexess.messenger_tech_03.Fragments.AboutFragment;
import com.zanexess.messenger_tech_03.Fragments.AboutUserFragment;
import com.zanexess.messenger_tech_03.Fragments.ChangeAboutFragment;
import com.zanexess.messenger_tech_03.Fragments.ChatFragment;
import com.zanexess.messenger_tech_03.Fragments.CreateChatFragment;
import com.zanexess.messenger_tech_03.Fragments.LoginFragment;
import com.zanexess.messenger_tech_03.Fragments.SettingsFragment;
import com.zanexess.messenger_tech_03.Fragments.SplashFragment;
import com.zanexess.messenger_tech_03.Messages.ChannelListData;
import com.zanexess.messenger_tech_03.Messages.LoginData;
import com.zanexess.messenger_tech_03.Messages.Message;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final String LOG_TAG = getClass().getSimpleName();
    private IService messengerService;
    private boolean connected = false;
    private FragmentManager fragmentManager;
    private SharedPreferences sPref;
    private LoginFragment loginFragment = null;
    private ChatFragment chatFragment = null;
    private Toolbar toolbar;
    private TextView nickname;
    private TextView status;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, final IBinder service) {
            messengerService = IService.Stub.asInterface(service);
            try {
                messengerService.bindActivity(mServiceCallback);
                connected = true;
                //Подгрузка данных, если они есть.
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadText();
                        //Подгрузка ника и инфы в навигейшн при авт. входе
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    nickname.setText(messengerService.getNick());
//                                    status.setText(messengerService.getCid());
//                                    checkChannels();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }, 300);
                    }
                }, 1900);
                Log.v(LOG_TAG, "Активити подключено");
            }
            catch (RemoteException e) {
                Log.v(LOG_TAG, "Ошибка подключения к сервису");
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.v(LOG_TAG, "Активити отключено");
            connected = false;
        }
    };

    public void checkChannels() {
        //Подгружаем список чатов
        try {
            Message message = new Message("channellist", new ChannelListData(messengerService.getCid(), messengerService.getSid()));
            Gson gson = new Gson();
            String mes = gson.toJson(message);
            messengerService.sendMessage(mes);
        } catch (RemoteException e) {

        }
    }

    private class ServiceCallback extends ICallback.Stub {

        @Override
        public void onNewMessage(String data) throws RemoteException {
            if (data.equals("LOGIN_FAILED_ERR")) {
                deleteText();
            } else if (data.equals("LOGIN_SUCCESS")) {
                loginFragment.isLogin();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        init();
                    }
                });
            } else if (data.equals("REFRESH_CHANNEL")) {
                chatFragment.refreshChannel();
            } else if (data.equals("REAUTH")) {
                loadText();
            }
        }

        @Override
        public void sendToUI(final String result) throws RemoteException {
            Log.v(LOG_TAG, result);
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onNewUser(String nick, String status) throws RemoteException {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.addToBackStack("user_info");
            AboutUserFragment aboutUserFragment = new AboutUserFragment();
            aboutUserFragment.setInfo(nick, status);
            ft.replace(R.id.main_frame_layout, aboutUserFragment);
            ft.commit();
        }
    }

    private ServiceCallback mServiceCallback = new ServiceCallback();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_after_login);
        fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.main_frame_layout, new SplashFragment());
        ft.commit();

        if (messengerService == null) {
            Intent i = new Intent(MainActivity.this, MessageSocketService.class);
            bindService(i, mServiceConnection, BIND_AUTO_CREATE);
        }


    }

    public void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.app_name, R.string.app_name);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

        View header = navigationView.inflateHeaderView(R.layout.nav_header_main);

        nickname = (TextView) header.findViewById(R.id.nickname_nav);
        status = (TextView) header.findViewById(R.id.status_nav);
        Log.v(LOG_TAG, "INIT");


        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                AboutFragment aboutFragment = new AboutFragment();
                try {
                    aboutFragment.setInfo(messengerService.getNick(), messengerService.getSid(), messengerService.getCid());
                    nickname.setText(messengerService.getNick());
                    status.setText(messengerService.getCid());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                ft.replace(R.id.main_frame_layout, aboutFragment);
                ft.commit();
                setTitle("Мой профиль");
                drawer.closeDrawer(Gravity.LEFT);
            }
        });
    }

    @Override
    protected void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    private void disconnect() {
        if (messengerService != null) {
            unbindService(mServiceConnection);
        }
    }

    public IService getMessengerService() {
        return messengerService;
    }

    public void saveText(String login, String password) {
        Log.v(LOG_TAG, "DATA_SAVED");
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putString("LOGIN", login);
        ed.putString("PASS", password);
        ed.commit();
    }

    public void deleteText() {
        Log.v(LOG_TAG, "DATA_DELETED");
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.remove("LOGIN");
        ed.remove("PASS");
        ed.commit();
    }

    private boolean loadText() {
        Log.v(LOG_TAG, "DATA_LOADED");
        sPref = getPreferences(MODE_PRIVATE);
        String login = sPref.getString("LOGIN", "");
        String pass = sPref.getString("PASS", "");
        if (!login.equals("") || !pass.equals("")) {
            Message message = new Message("auth", new LoginData(login, pass));
            Gson gson = new Gson();
            String msg = gson.toJson(message);
            try {
                messengerService.sendMessage(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            return true;
        }
        return false;
    }

    public void setLoginFragment(LoginFragment loginFragment) {
        this.loginFragment = loginFragment;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.add_chat_menu_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame_layout, new CreateChatFragment());
            ft.commit();
        } else if (id == R.id.change_status_menu_item) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame_layout, new ChangeAboutFragment());
            ft.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_message) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            chatFragment = new ChatFragment();
            ft.replace(R.id.main_frame_layout, chatFragment);
            ft.commit();
        } else if (id == R.id.nav_settings) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame_layout, new SettingsFragment());
            ft.commit();
        } else if (id == R.id.nav_exit) {
            deleteText();
            Toast.makeText(MainActivity.this, "Авторизационные данные сброшены. Перезапустите приложение", Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public TextView getNickname() {
        return nickname;
    }

    public TextView getStatus() {
        return status;
    }
}
