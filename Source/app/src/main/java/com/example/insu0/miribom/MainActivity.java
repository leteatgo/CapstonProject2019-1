package com.example.insu0.miribom;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Servers.MiribomInfo;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Button main_kakaoBtn;
    ImageButton main_emailBtn;
    TextView main_joinText;


    /*Naver Login Module*/
    private OAuthLoginButton main_naverBtn;
    private JSONObject naverUserAccountInfo;
//    private Map<String,String> myUserInfoMap; //여기에 네이버 유저 정보 담김

    private static String CLIENT_ID = "aePp5k4iytz4eAsaenxW";
    private static String CLIENT_SECRET = "fJfOxprZL6";
    private static String CLIENT_NAME = "미리봄";
    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;


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

        mContext = this;
        initNaver();

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

        main_naverBtn = (OAuthLoginButton) findViewById(R.id.main_naverBtn);
        main_naverBtn.setOAuthLoginHandler(mOAuthLoginHandler);
        main_naverBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                mOAuthLoginInstance.startOauthLoginActivity( MainActivity.this, mOAuthLoginHandler);

            }
        });
    }
    private void initNaver(){
        //Naver Instance Initialization
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext,CLIENT_ID,CLIENT_SECRET,CLIENT_NAME);
    }

    @Override
    protected void onResume() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        super.onResume();

    }

    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                Log.d(TAG,accessToken);
                Log.d(TAG,refreshToken);
                Log.d(TAG,String.valueOf(expiresAt));
                Log.d(TAG,tokenType);
                Log.d(TAG, mOAuthLoginInstance.getState(mContext).toString());
                new RequestApiTask().execute();
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
                Log.d(TAG,"errorDesc:"+ errorDesc);
            }
        };
    };

    private class DeleteTokenTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            boolean isSuccessDeleteToken = mOAuthLoginInstance.logoutAndDeleteToken(mContext);

            if (!isSuccessDeleteToken) {
                // 서버에서 token 삭제에 실패했어도 클라이언트에 있는 token 은 삭제되어 로그아웃된 상태이다
                // 실패했어도 클라이언트 상에 token 정보가 없기 때문에 추가적으로 해줄 수 있는 것은 없음
                Log.d(TAG, "errorCode:" + mOAuthLoginInstance.getLastErrorCode(mContext));
                Log.d(TAG, "errorDesc:" + mOAuthLoginInstance.getLastErrorDesc(mContext));
            }

            return null;
        }
    }

    private class RequestApiTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            String url = "https://openapi.naver.com/v1/nid/getUserProfile.xml";
            String at = mOAuthLoginInstance.getAccessToken(mContext);
            String data = mOAuthLoginInstance.requestApi(mContext, at, url);
            naverUserAccountInfo = requestNaverUserInfo(data);
//            myUserInfoMap = requestNaverUserInfo(data);
            return data;
        }

        protected void onPostExecute(String data) {
            try {
                if (naverUserAccountInfo.getInt("resultcode") == 00) {
                    Toast.makeText(mContext, "안녕하세요 " + naverUserAccountInfo.get("name")+ "님!", Toast.LENGTH_LONG).show();
                    //  send server to login
                    new SignInByNaverTask().execute("http://" + MiribomInfo.ipAddress + "/login/signIn/nhn");
                }
                else
                    Toast.makeText(mContext, "로그인에 실패하였습니다.", Toast.LENGTH_LONG).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private JSONObject requestNaverUserInfo(String data) { // xml 파싱
//        private Map<String, String> requestNaverUserInfo(String data) { // xml 파싱
            String f_array[] = new String[9];
//            Map<String, String> resultMap = new HashMap<>();
            naverUserAccountInfo = new JSONObject();
            try {
                XmlPullParserFactory parserCreator = XmlPullParserFactory
                        .newInstance();
                XmlPullParser parser = parserCreator.newPullParser();
                InputStream input = new ByteArrayInputStream(
                        data.getBytes("UTF-8"));
                parser.setInput(input, "UTF-8");

                int parserEvent = parser.getEventType();
                String tag = "";
                boolean inText = false;
                boolean lastMatTag = false;

                while (parserEvent != XmlPullParser.END_DOCUMENT) {
                    switch (parserEvent) {
                        case XmlPullParser.START_TAG:
                            tag = parser.getName();
                            if (tag.compareTo("xml") == 0) {
                                inText = false;
                            } else if (tag.compareTo("data") == 0) {
                                inText = false;
                            } else if (tag.compareTo("result") == 0) {
                                inText = false;
                            } else if (tag.compareTo("response") == 0) {
                                inText = false;
                            } else {
                                inText = true;

                            }
                            break;
                        case XmlPullParser.TEXT:
//                            Log.d(TAG, "requestNaverUserInfo: tag name: " + tag);
                            if (inText) {   //  true 받을 값 이면
                                if (parser.getText() == null) {
//                                    resultMap.put(tag, "");
                                    naverUserAccountInfo.put(tag,"");
                                } else {
//                                    resultMap.put(tag,parser.getText().trim());
                                    naverUserAccountInfo.put(tag, parser.getText().trim());
                                }
//                                Log.d(TAG, "requestNaverUserInfo: text: "+ parser.getText());
                            }
                            inText = false;
                            break;
                        case XmlPullParser.END_TAG:
                            tag = parser.getName();
                            inText = false;
                            break;
                    }

                    parserEvent = parser.next();
                }
            } catch (Exception e) {
                Log.e("dd", "Error in network call", e);
            }

            return naverUserAccountInfo;
        }

    }


    public class SignInByNaverTask extends AsyncTask<String, String, String> {
        String TAG = "SignInByNaverTask>>>";

        @Override
        protected String doInBackground(String... strings) {    //  id로 서버에서 salt hash 가져옴
            try {
                JSONObject loginInfo = new JSONObject();
                loginInfo.accumulate("id", naverUserAccountInfo.getString("email"));
                String salt = BCrypt.gensalt();
                String hash = BCrypt.hashpw(naverUserAccountInfo.getString("id"), salt);
                loginInfo.accumulate("hash", hash);
                loginInfo.accumulate("salt", salt);
                loginInfo.accumulate("name", naverUserAccountInfo.getString("name"));

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
            Log.d(TAG, "onPostExecute: " + result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("no", jsonObject.getInt("no"));
                intent.putExtra("id", naverUserAccountInfo.getString("email"));
//                Log.d(TAG, "onPostExecute: json");
                startActivity(intent);
            } catch (JSONException e) {
                int no = Integer.parseInt(result.substring(1, result.length() - 1));
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                intent.putExtra("no", no);
                try {
                    intent.putExtra("id", naverUserAccountInfo.getString("email"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
//                Log.d(TAG, "onPostExecute: catch");
                startActivity(intent);
            }
        }
    }
}