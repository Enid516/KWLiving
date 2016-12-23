package com.enid.kwliving;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.enid.kwliving.server.ServerUDPBoardCast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectServer(View view){
        startActivity(new Intent(this,ServerActivity.class));
    }

    public void selectClient(View view){
        startActivity(new Intent(this,ClientActivity.class));
    }
}
