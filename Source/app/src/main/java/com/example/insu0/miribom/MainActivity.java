package com.example.insu0.miribom;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Servers.MiribomInfo;

public class MainActivity extends AppCompatActivity {

    Button main_kakaoBtn;
    Button main_naverBtn;
    Button main_emailBtn;
    TextView main_joinText;


    /*
    * @author ckddn
    * added 190412
    * */
    @Override
    protected void onStart() {

        super.onStart();

        if (android.support.v4.app.ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && android.support.v4.app.ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            android.support.v4.app.ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        /* give and check permission(INTERNET) */
        ActivityCompat.requestPermissions(this, MiribomInfo.common_permissions, PackageManager.PERMISSION_GRANTED);
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        main_joinText = findViewById(R.id.main_jointext);
        main_joinText.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        main_emailBtn = findViewById(R.id.main_emailBtn);
        main_emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
