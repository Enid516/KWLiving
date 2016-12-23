package com.enid.kwliving;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.enid.kwliving.client.ClientUDPReceiver;

/**
 * Created by big_love on 2016/12/16.
 */

public class ClientActivity extends BaseActivity {
    private static final String TAG = "ClientActivity";
    private TextView textView;
    private EditText editText;
    private ClientUDPReceiver clientReceiver;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);
        clientReceiver = new ClientUDPReceiver(this);

        clientReceiver.setListener(new ClientUDPReceiver.ClientReceiverCallBack() {
            @Override
            public void onResponse(String msg) {
                textView.setText(textView.getText().toString() + msg + "\n");
            }

            @Override
            public void onConnected(String content) {
                dismissProgressDialog();
                textView.setText(textView.getText().toString() + content +"\n");
            }
        });
        showProgressDialog();
    }

    public void clickSend(View view) {
        if (!TextUtils.isEmpty(editText.getText().toString())) {
            clientReceiver.sendMessage(editText.getText().toString());
            editText.setText("");
        } else {
            Toast.makeText(this, "请输入发送内容", Toast.LENGTH_SHORT);
        }
    }

    private void showProgressDialog() {
        if (dialog == null) {
            dialog = new ProgressDialog(this);
        }
        dialog.show();
    }

    private void dismissProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        clientReceiver.sendMessage("exit");
        Log.i(TAG,"onDestroy client exit");
    }
}
