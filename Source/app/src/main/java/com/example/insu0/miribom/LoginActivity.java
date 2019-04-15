package com.example.insu0.miribom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.UserData;
import com.example.insu0.miribom.Servers.MiribomInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
 * @author ckddn
 * implemented 190415
 * */
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailInput;
    private EditText login_passwordInput;
    private Button loginBtn;
    private Button login_prevBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = (EditText) findViewById(R.id.emailInput);
        login_passwordInput = (EditText) findViewById(R.id.passwordInput);


        /*
         * @author ckddn
         * added 190415
         * */
        try {   //  Only after signUp...
            String id = getIntent().getStringExtra("id");
            emailInput.setText(id);
        } catch(Exception e){
            e.printStackTrace();
        }



        loginBtn = (Button)findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        new SignInTask().execute("http://" + MiribomInfo.ipAddress + "/login/signIn");
    }

    public class SignInTask extends AsyncTask<String, String, String> {
        String TAG = "SignInTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject loginInfo = new JSONObject();
                loginInfo.accumulate("id", emailInput.getText().toString());
                loginInfo.accumulate("pw", login_passwordInput.getText().toString());
                HttpURLConnection conn = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL(strings[0]);
                    // settings
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Cache-Control", "no-cache");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setRequestProperty("Accept", "application/text");
                    conn.setRequestProperty("Accept", "application/json");
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.connect();

                    OutputStream outputStream = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
                    writer.write(loginInfo.toString());
                    writer.flush();
                    writer.close();

                    InputStream stream = conn.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line = "";
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line);
                        Log.d(TAG, "doInBackground: readLine, " + line);
                    }
                    return buffer.toString();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("OK")) {
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.putExtra("id", emailInput.getText().toString());
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }
        }
    }
}
