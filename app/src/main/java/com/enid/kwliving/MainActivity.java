package com.enid.kwliving;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.enid.kwliving.client.view.ClientHomeActivity;
import com.enid.kwliving.server.ServerUDPBoardCast;
import com.enid.kwliving.server.view.ServerHomeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectServer(View view){
        startActivity(new Intent(this,ServerHomeActivity.class));
    }

    public void selectClient(View view){
        startActivity(new Intent(this,ClientHomeActivity.class));
    }
}
