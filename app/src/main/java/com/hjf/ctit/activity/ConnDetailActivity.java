package com.hjf.ctit.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hjf.ctit.R;
import com.hjf.ctit.entity.Connection;
import com.hjf.ctit.entity.Connections;
import com.hjf.ctit.fragment.AlertDialogFragment;
import com.hjf.ctit.fragment.MsgFragment;
import com.hjf.ctit.fragment.OtherFragment;
import com.hjf.ctit.listener.ActionListener;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 * @author HJianFei
 * @description 连接详情类
 * @Date 2016-05-22
 */
public class ConnDetailActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private String clientHandle = null;
    private Connection connection = null;
    private ChangeListener changeListener = null;
    private MyPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clientHandle = getIntent().getStringExtra("handle");
        setContentView(R.layout.activity_conn_detail);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(mViewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        connection = Connections.getInstance(this).getConnection(clientHandle);
        changeListener = new ChangeListener();
        connection.registerChangeListener(changeListener);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.addSub:
                Bundle bundle = new Bundle();
                bundle.putString("handler", clientHandle);
                AlertDialogFragment dialog = new AlertDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(getSupportFragmentManager(), "alert");
                break;
            case R.id.disconnect:
                connect();
                break;
            case R.id.connect:
                disconnect();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        int menuID;
        boolean connected = Connections.getInstance(this).getConnection(clientHandle).isConnected();
        if (connected) {
            menuID = R.menu.menu_main_disconnect;
        } else {
            menuID = R.menu.menu_main_connect;
        }
        getMenuInflater().inflate(menuID, menu);
        return true;

    }

    private void connect() {

        Connections.getInstance(this).getConnection(clientHandle)
                .changeConnectionStatus(Connection.ConnectionStatus.CONNECTING);

        Connection c = Connections.getInstance(this).getConnection(clientHandle);
        try {
            c.getClient().connect(c.getConnectionOptions(), null,
                    new ActionListener(this, ActionListener.Action.CONNECT, clientHandle, null));
        } catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle,
                    e);
            c.addAction("连接失败");
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to reconnect the client with the handle " + clientHandle,
                    e);
            c.addAction("连接失败");
        }
    }

    private void disconnect() {
        Connection c = Connections.getInstance(this).getConnection(clientHandle);

        if (!c.isConnected()) {
            return;
        }

        try {
            c.getClient().disconnect(null, new ActionListener(this, ActionListener.Action.DISCONNECT, clientHandle, null));
            c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTING);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "Failed to disconnect the client with the handle " + clientHandle,
                    e);
            c.addAction("连接失败");
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MsgFragment(), "商品信息");
        Bundle bundle=new Bundle();
        bundle.putString("clientHandle",clientHandle);
        OtherFragment otherFragment = new OtherFragment();
        otherFragment.setArguments(bundle);
        adapter.addFragment(otherFragment, "订阅信息");
        viewPager.setAdapter(adapter);
    }


    static class MyPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        connection.removeChangeListener(null);
        super.onDestroy();
    }

    private class ChangeListener implements PropertyChangeListener {


        @Override
        public void propertyChange(PropertyChangeEvent event) {
            // connection object has change refresh the UI

            ConnDetailActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //动态改变菜单布局
                    ConnDetailActivity.this.invalidateOptionsMenu();
//                    ((MsgFragment) adapter.getItem(0)).refresh();
                    ((OtherFragment) adapter.getItem(1)).refresh();

                }
            });

        }
    }
}
