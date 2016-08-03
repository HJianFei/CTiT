package com.hjf.ctit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hjf.ctit.R;
import com.hjf.ctit.constants.ActivityConstants;
import com.hjf.ctit.utils.Notify;

/**
 * @description 新建连接
 */
public class NewConnActivity extends AppCompatActivity implements View.OnClickListener {

    private Bundle result = null;
    private Button btnAdd, btnCancle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conn);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnCancle = (Button) findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(this);
        btnCancle.setOnClickListener(this);
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
    public void onClick(View v) {
        Intent dataBundle = new Intent();
        switch (v.getId()) {
            case R.id.btnAdd:
                String server = ((EditText) findViewById(R.id.edit_server_ip))
                        .getText().toString();
                String port = ((EditText) findViewById(R.id.edit_server_port))
                        .getText().toString();
                String clientId = ((EditText) findViewById(R.id.edit_client_name))
                        .getText().toString();
                if (server.equals(ActivityConstants.empty) || port.equals(ActivityConstants.empty) || clientId.equals(ActivityConstants.empty)) {
                    String notificationText = this.getString(R.string.missingOptions);
                    Notify.toast(NewConnActivity.this, notificationText, Toast.LENGTH_SHORT);
                    return;
                }
                dataBundle.putExtra(ActivityConstants.server, server);
                dataBundle.putExtra(ActivityConstants.port, port);
                dataBundle.putExtra(ActivityConstants.clientId, clientId);
                dataBundle.putExtra(ActivityConstants.action, ActivityConstants.connect);
                dataBundle.putExtra(ActivityConstants.cleanSession, true);
//                if (result == null) {
//                    result = new Bundle();
//                    result.putString(ActivityConstants.message, ActivityConstants.empty);
//                    result.putString(ActivityConstants.topic, ActivityConstants.empty);
//                    result.putInt(ActivityConstants.qos, ActivityConstants.defaultQos);
//                    result.putBoolean(ActivityConstants.retained, ActivityConstants.defaultRetained);
//                }
//                dataBundle.putExtras(result);
                setResult(RESULT_OK, dataBundle);
                NewConnActivity.this.finish();
                break;
            case R.id.btnCancel:
                NewConnActivity.this.finish();
        }
    }

}


