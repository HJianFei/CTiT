package com.hjf.ctit.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.hjf.ctit.R;
import com.hjf.ctit.adapter.CommonAdapter;
import com.hjf.ctit.adapter.ViewHolder;
import com.hjf.ctit.constants.ActivityConstants;
import com.hjf.ctit.constants.Constants;
import com.hjf.ctit.entity.Connection;
import com.hjf.ctit.entity.Connections;
import com.hjf.ctit.listener.ActionListener;
import com.hjf.ctit.mqttutils.MqttCallbackHandler;
import com.hjf.ctit.mqttutils.MqttTraceCallback;
import com.hjf.ctit.utils.SharedPreferencesUtils;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author HJianFei
 * @description 程序入口
 * @Date 2016-05-22
 */
public class MainActivity extends AppCompatActivity {

    //浮动按钮
    private FloatingActionButton mFabButton;
    //事件监听
    private ChangeListener changeListener = new ChangeListener();
    private MainActivity clientConnections = this;
    // Connection适配器
    private CommonAdapter<Connection> mAdapter = null;
    private ArrayList<Connection> lists = new ArrayList<>();
    private ListView lv_all;
    private long exitTime = 0;
    private Map<String, Connection> mConnections;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化数据
        initData();
        //初始化View
        initView();
        //浮动按钮的点击事件
        setUpFAB();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        // 获取全部可用的连接
        mConnections = Connections.getInstance(this).getConnections();

        if (mConnections != null) {
            for (String s : mConnections.keySet()) {
                lists.add(mConnections.get(s));
            }
        }
    }

    /**
     * 初始化
     */
    private void initView() {

        lv_all = (ListView) findViewById(R.id.lv_all);
        mAdapter = new CommonAdapter<Connection>(this, lists, R.layout.connection_text_view) {
            @Override
            public void convert(ViewHolder helper, Connection item) {
                helper.setText(R.id.tv, item.toString());
            }
        };
        lv_all.setAdapter(mAdapter);
        lv_all.setOnItemLongClickListener(new LongItemClickListener());
        lv_all.setOnItemClickListener(new ItemClickListener());

    }

    /**
     * 浮动按钮及其点击事件
     */
    private void setUpFAB() {
        mFabButton = (FloatingActionButton) findViewById(R.id.fab_normal);
        mFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NewConnActivity.class);
                startActivityForResult(intent, ActivityConstants.REQCODE);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        mConnections = Connections.getInstance(clientConnections).getConnections();
        for (Connection connection : mConnections.values()) {
            connection.getClient().registerResources(this);
            connection.getClient().setCallback(new MqttCallbackHandler(this, connection.getClient().getServerURI() + connection.getClient().getClientId()));
        }
    }

    @Override
    protected void onDestroy() {

        Map<String, Connection> connections = Connections.getInstance(this).getConnections();

        for (Connection connection : connections.values()) {
            connection.registerChangeListener(changeListener);
            connection.getClient().unregisterResources();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        Bundle dataBundle = data.getExtras();
        connectAction(dataBundle);
    }

    @Override
    public void onBackPressed() {

        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
        }

    }

    /**
     * 建立连接，进行监听
     *
     * @param data
     */
    private void connectAction(Bundle data) {

        MqttConnectOptions conOpt = new MqttConnectOptions();
        String server = (String) data.get(ActivityConstants.server);
        String clientId = (String) data.get(ActivityConstants.clientId);
        int port = Integer.parseInt((String) data.get(ActivityConstants.port));
        String uri = "tcp://" + server + ":" + port;
        MqttAndroidClient client = Connections.getInstance(this).createClient(this, uri, clientId);
        String clientHandle = uri + clientId;
        Connection connection = new Connection(clientHandle, clientId, server, port, this, client);
        lists.add(connection);
        connection.registerChangeListener(changeListener);
        // connect client
        String[] actionArgs = new String[1];
        actionArgs[0] = clientId;
        connection.changeConnectionStatus(Connection.ConnectionStatus.CONNECTING);
        //清除缓存
        conOpt.setCleanSession(true);
        final ActionListener callback = new ActionListener(getApplicationContext(), ActionListener.Action.CONNECT, clientHandle,
                actionArgs);

        //添加回调
        client.setCallback(new MqttCallbackHandler(this, clientHandle));
        client.setTraceCallback(new MqttTraceCallback());
        connection.addConnectionOptions(conOpt);

        //添加数据库SQLite
        Connections.getInstance(this).addConnection(connection);

        try {
            //建立连接
            client.connect(conOpt, null, callback);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(), "MqttException Occured", e);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_ip_address) {
            final EditText editText = new EditText(this);
            editText.setHint("服务器IP:example.example.com");
            editText.setTextSize(20);
            new AlertDialog.Builder(this).setTitle("设置服务器IP地址")
                    .setIcon(R.mipmap.ic_launcher).setView(editText)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            SharedPreferencesUtils.setParam(MainActivity.this,
                                    Constants.DB_ADDRESS, editText.getText()
                                            .toString().trim());
                            Toast.makeText(MainActivity.this, "地址设置成功",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("取消", null).show();
            return true;
        }
        if (id == R.id.now_ip_address) {
            new AlertDialog.Builder(this)
                    .setTitle("当前服务器IP地址")
                    .setIcon(R.mipmap.ic_launcher)
                    .setMessage(
                            (String) SharedPreferencesUtils.getParam(
                                    MainActivity.this, Constants.DB_ADDRESS,
                                    Constants.DEFAULT_DB_ADDRESS))
                    .setPositiveButton("确定", null).show();
            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 推送事件状态变化监听
     */

    private class ChangeListener implements PropertyChangeListener {
        @Override
        public void propertyChange(PropertyChangeEvent event) {

            if (!event.getPropertyName().equals(ActivityConstants.ConnectionStatusProperty)) {
                return;
            }
            clientConnections.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    clientConnections.mAdapter.notifyDataSetChanged();
                }

            });

        }
    }

    /**
     * listView Item 的长点击事件
     */

    private class LongItemClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            final Connection connection;
            connection = mAdapter.getItem(position);
            if (connection.isConnectedOrConnecting()) {

                AlertDialog.Builder builder = new AlertDialog.Builder(clientConnections);
                builder.setTitle(R.string.disconnectClient).setMessage(getString(R.string.deleteDialog))
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            connection.getClient().disconnect();
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        lists.remove(connection);
                        mAdapter.notifyDataSetChanged();
                        Connections.getInstance(clientConnections).removeConnection(connection);

                    }
                }).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(clientConnections);
                builder.setTitle(R.string.disconnectClient).setMessage("确定删除此连接？")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        lists.remove(connection);
                        mAdapter.notifyDataSetChanged();
                        Connections.getInstance(clientConnections).removeConnection(connection);
                    }
                }).show();
            }
            return true;
        }
    }

    /**
     * Item 的点击事件
     */

    private class ItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Connection c = mAdapter.getItem(position);

            Intent intent = new Intent(MainActivity.this, ConnDetailActivity.class);
            intent.putExtra("handle", c.handle());
            startActivity(intent);
        }
    }
}