package com.enid.kwliving;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.enid.kwliving.server.ServerUDPBoardCast;

/**
 * Created by big_love on 2016/12/16.
 */

public class ServerActivity extends BaseActivity {

    private ServerUDPBoardCast serverUDPBoardcast;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.editText);

        serverUDPBoardcast = new ServerUDPBoardCast(this);
        serverUDPBoardcast.start();
        serverUDPBoardcast.setListener(new ServerUDPBoardCast.ServiceResponseCallBack() {
            @Override
            public void onResponse(String s) {
                textView.setText(textView.getText().toString() + "\n" + s);
            }
        });
    }

    public void selectSend(View view) {
        serverUDPBoardcast.sendClientMsg(0,editText.getText().toString());
        editText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverUDPBoardcast.exit();
    }
}
