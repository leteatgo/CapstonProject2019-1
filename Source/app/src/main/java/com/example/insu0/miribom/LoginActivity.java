package com.example.insu0.miribom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.insu0.miribom.Data.UserData;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText login_passwordInput;
    private Button loginBtn;
    private Button login_prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();

    }
}
