package com.hjf.ctit.mqttutils;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.hjf.ctit.R;
import com.hjf.ctit.activity.ConnDetailActivity;
import com.hjf.ctit.activity.MainActivity;
import com.hjf.ctit.entity.Connection;
import com.hjf.ctit.entity.Connections;
import com.hjf.ctit.entity.MessageBean;
import com.hjf.ctit.utils.Notify;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackHandler implements MqttCallback {

    private Context context;
    private String clientHandle;
    private String mHex = "";

    public MqttCallbackHandler(Context context, String clientHandle) {
        this.context = context;
        this.clientHandle = clientHandle;
    }

    @Override
    public void connectionLost(Throwable cause) {
        if (cause != null) {
            Connection c = Connections.getInstance(context).getConnection(clientHandle);
            c.addAction("连接中断");
            c.changeConnectionStatus(Connection.ConnectionStatus.DISCONNECTED);
            Object[] args = new Object[2];
            args[0] = c.getId();
            args[1] = c.getHostName();
            String message = context.getString(R.string.connection_lost, args);
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("handle", clientHandle);
            // 状态栏通知
            Notify.notifcation(context, message, intent, R.string.notifyTitle_connectionLost);
        }
    }

    /**
     * 接收到推送的通知
     */

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {

        Connection c = Connections.getInstance(context).getConnection(clientHandle);
        final String messageString = new String(message.getPayload());
        Intent intent = new Intent(context, ConnDetailActivity.class);
        intent.putExtra("handle", clientHandle);

        Gson gson = new Gson();
        MessageBean messageBean = gson.fromJson(messageString, MessageBean.class);
//        System.out.println("Tag:" + messageBean.getTag().getHex());
//        System.out.println("ReaderID:" + messageBean.getReaderID());
//        System.out.println("Timestamp:" + messageBean.getTimestamp());
        if (!mHex.equals(messageBean.getTag().getHex())) {
            mHex = messageBean.getTag().getHex();
            Notify.notifcation(context, messageBean.getTag().getHex(), intent, R.string.notifyTitle);
            c.addAction("TOPIC: " + messageBean.getReaderID() + "\nHEX: " + messageBean.getTag().getHex());
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        // Do nothing
    }

}
