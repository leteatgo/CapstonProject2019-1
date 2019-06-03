package com.example.insu0.miribom;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.insu0.miribom.Data.CatListViewAdapter;
import com.example.insu0.miribom.Data.CatListViewItem;
import com.example.insu0.miribom.Servers.MiribomInfo;

import org.json.JSONArray;
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

//implemented by Yang Insu
public class CatSearchActivity extends AppCompatActivity {

    private ListView catlistview;
    private CatListViewAdapter catListViewAdapter;
    private int f_type;
    //Receive Object


    @Override
    protected void onStart() {
        super.onStart();
        f_type = getIntent().getIntExtra("f_type", -1);
        catListViewAdapter = new CatListViewAdapter();

        catlistview = findViewById(R.id.catlistView);
        catlistview.setAdapter(catListViewAdapter);

        init();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cat_search);

    }

    public void init(){
        new CategorySearchTask().execute("http://" + MiribomInfo.ipAddress + "/home/search/category/" + f_type);

//        catListViewAdapter.addItem(getResources().get);
//        catListViewAdapter.addItem(new Bitm);
//        catListViewAdapter.addItem(ContextCompat.getDrawable(this,R.drawable.chinese),"Chinese Restaurant", "very tasty chinese cusine");
    }

    /*  For Category Search */
    public class CategorySearchTask extends AsyncTask<String, String, String> {
        String TAG = "CategorySearchTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            JSONObject reqInfo = new JSONObject();

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
                writer.write(reqInfo.toString());
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
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
            try {
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    int no = object.getInt("no");
                    String name = object.getString("name");
                    String imageUrl = object.getString("image");
                    String address = object.getString("address");
                    catListViewAdapter.addItem(new CatListViewItem(no, imageUrl, name, address, catListViewAdapter));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            catListViewAdapter.notifyDataSetChanged();
        }
    }


}
