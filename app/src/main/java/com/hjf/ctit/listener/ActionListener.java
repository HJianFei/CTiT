package com.hjf.ctit.listener;

import android.content.Context;
import android.widget.Toast;

import com.hjf.ctit.R;
import com.hjf.ctit.entity.Connection;
import com.hjf.ctit.entity.Connections;
import com.hjf.ctit.utils.Notify;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;

public class ActionListener implements IMqttActionListener {


    public enum Action {

        CONNECT,

        DISCONNECT,

        SUBSCRIBE,

        PUBLISH
    }

    private Action action;

    private String[] additionalArgs;

    private String clientHandle;

    private Context context;

    public ActionListener(Context context, Action action,
                          String clientHandle, String... additionalArgs) {
        this.context = context;
        this.action = action;
        this.clientHandle = clientHandle;
        this.additionalArgs = additionalArgs;
    }


    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        switch (action) {
            case CONNECT:
                connect();
                break;
            case DISCONNECT:
                disconnect();
                break;
            case SUBSCRIBE:
                subscribe();
                break;
            case PUBLISH:
                publish();
                break;
        }

    }


    private void publish() {

        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        String actionTaken = context.getString(R.string.toast_pub_success,
                (Object[]) additionalArgs);
        c.addAction(actionTaken);
        Notify.toast(context, actionTaken, Toast.LENGTH_SHORT);
    }


    private void subscribe() {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        String actionTaken = context.getString(R.string.toast_sub_success,
                (Object[]) additionalArgs);
        c.addAction(actionTaken);
        Notify.toast(context, actionTaken, Toast.LENGTH_SHORT);

    }

    private void disconnect() {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
//        String actionTaken = context.getString(R.string.toast_disconnected);
//        c.addAction(actionTaken);

    }


    private void connect() {

        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        c.changeConnectionStatus(Connection.ConnectionStatus.CONNECTED);
//        c.addAction("成功连接服务器");

    }


    @Override
    public void onFailure(IMqttToken token, Throwable exception) {
        switch (action) {
            case CONNECT:
                connect(exception);
                break;
            case DISCONNECT:
                disconnect(exception);
                break;
            case SUBSCRIBE:
                subscribe(exception);
                break;
            case PUBLISH:
                publish(exception);
                break;
        }

    }


    private void publish(Throwable exception) {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        String action = context.getString(R.string.toast_pub_failed,
                (Object[]) additionalArgs);
        c.addAction(action);
        Notify.toast(context, action, Toast.LENGTH_SHORT);

    }


    private void subscribe(Throwable exception) {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        String action = context.getString(R.string.toast_sub_failed,
                (Object[]) additionalArgs);
        c.addAction(action);
        Notify.toast(context, action, Toast.LENGTH_SHORT);

    }


    private void disconnect(Throwable exception) {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
        c.addAction("断开连接失败");

    }

    private void connect(Throwable exception) {
        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        c.changeConnectionStatus(Connection.ConnectionStatus.ERROR);
        c.addAction("连接失败");

    }

}