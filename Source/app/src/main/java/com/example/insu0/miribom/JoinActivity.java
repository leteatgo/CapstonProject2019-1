package com.example.insu0.miribom;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.insu0.miribom.Servers.MiribomInfo;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

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
 * completed 190415
 * */
public class JoinActivity extends AppCompatActivity {

    private static final String TAG = "JoinActivity";

    private Button join_prevBtn;
    private EditText join_emailInput;
    private EditText join_passwordInput;
    private EditText join_passwordVerify;
    private EditText join_nameInput;
    private EditText join_mobileInput;
    private Button joinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // init
        join_prevBtn = (Button) findViewById(R.id.join_prevBtn);
        join_prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        join_emailInput = (EditText) findViewById(R.id.join_emailInput);
        join_passwordInput = (EditText) findViewById(R.id.join_passwordInput);
        join_passwordVerify = (EditText) findViewById(R.id.join_passwordVerify);
        join_nameInput = (EditText) findViewById(R.id.join_nameInput);
        join_mobileInput = (EditText) findViewById(R.id.join_mobileInput);

        joinBtn = (Button) findViewById(R.id.joinBtn);
        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFilled()) {
                    if (join_passwordInput.getText().toString().equals(join_passwordVerify.getText().toString())) {
                        String salt = BCrypt.gensalt();
                        String hash = BCrypt.hashpw(join_passwordInput.getText().toString(), salt);
                        new SignUpTask().execute("http://" + MiribomInfo.ipAddress + "/join/signUp", salt, hash);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "비밀번호를 다시 한번 확인해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private boolean isFilled() {
        if (join_emailInput.getText().toString().equals("") || join_passwordInput.getText().toString().equals("")
                || join_passwordVerify.getText().toString().equals("")
                || join_nameInput.getText().toString().equals("")
                || join_mobileInput.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(),"모든 항목을 기입해주세요.", Toast.LENGTH_LONG).show();
            return false;
        }
        else return true;
    }

    public class EmailVerifier extends AsyncTask<String, String, String> {
        String TAG = "EmailVerifier>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject regInfo = new JSONObject();
                regInfo.accumulate("id", join_emailInput.getText().toString());

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
                    writer.write(regInfo.toString());
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
                Toast.makeText(getApplicationContext(), "인증메시지가 전송되었습니다.\n이메일을 확인하여 주세요!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "인증메시지 전송에 실패하였습니다.\n잠시 후 다시 시도 해주세요.", Toast.LENGTH_LONG).show();
            }
        }
    }


    public class SignUpTask extends AsyncTask<String, String, String> {
        String TAG = "SignUpTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject regInfo = new JSONObject();
                regInfo.accumulate("id", join_emailInput.getText().toString());
                regInfo.accumulate("salt", strings[1]);
                regInfo.accumulate("hash", strings[2]);
                regInfo.accumulate("name", join_nameInput.getText().toString());
                regInfo.accumulate("mobile", join_mobileInput.getText().toString());

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
                    writer.write(regInfo.toString());
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
            if (result.equals("OK")) { // 인증메시지 발송
                Toast.makeText(getApplicationContext(), "인증메시지가 발송되었습니다.\n이메일을 확인해 주세요.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                intent.putExtra("id", join_emailInput.getText().toString());
                startActivity(intent);
            } else { // ID 중복
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            }

        }
    }


}
