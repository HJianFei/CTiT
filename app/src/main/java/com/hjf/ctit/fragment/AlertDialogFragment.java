package com.hjf.ctit.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.hjf.ctit.R;
import com.hjf.ctit.activity.ConnDetailActivity;
import com.hjf.ctit.constants.ActivityConstants;
import com.hjf.ctit.entity.Connections;
import com.hjf.ctit.listener.ActionListener;
import com.hjf.ctit.utils.Notify;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;

/**
 * Created by HJianFei on 2016-5-28.
 *
 * @Decription 订阅主题
 */
public class AlertDialogFragment extends DialogFragment {
    private ConnDetailActivity mActivity;
    private String clientHandle = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        clientHandle = (String) arguments.get("handler");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_dialog, null);
        builder.setView(view)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                subscribe(view);
                            }
                        }).setNegativeButton("取消", null);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (ConnDetailActivity) context;
    }

    private void subscribe(View view) {
        String topic = ((EditText) view.findViewById(R.id.topic)).getText().toString();
        if (topic.equals(ActivityConstants.em)) {
            Notify.toast(mActivity, "订阅主题不能为空", Toast.LENGTH_SHORT);
            return;
        }

        try {
            String[] topics = new String[1];
            topics[0] = topic;
            Connections.getInstance(mActivity).getConnection(clientHandle).getClient().subscribe(topic, 2, null,
                    new ActionListener(mActivity, ActionListener.Action.SUBSCRIBE, clientHandle, topics));
        } catch (MqttSecurityException e) {
            Log.e(this.getClass().getCanonicalName(),
                    "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        } catch (MqttException e) {
            Log.e(this.getClass().getCanonicalName(),
                    "Failed to subscribe to" + topic + " the client with the handle " + clientHandle, e);
        }
    }
}
