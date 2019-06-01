package com.example.insu0.miribom.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.insu0.miribom.Lists.HomeRestaurantListItem;
import com.example.insu0.miribom.R;
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


@SuppressLint("ValidFragment")
public class InfoFragment extends Fragment
{
    public final static String TAG = "InfoFragment";
    private Context context;
    private HomeRestaurantListItem restaurant;
    private int resNo;
    private String mobile;
    private String address;
    private String hours;
    private String image;
    private byte[] imageDatas;
    private int totalPlaceNum, availablePlaceNum, reservablePlaceNum;

    private TextView lunchtimeTextView;
    private TextView telTextView;
    private TextView addrTextView;
    private TextView seatinfoTextView;
    private ImageView resMenuImage;

    @SuppressLint("ValidFragment")
    public InfoFragment(HomeRestaurantListItem item, Context context) {
        this.restaurant = item;
        this.resNo = item.getResNo();
        this.context = context;

    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_res_info, container, false);
        telTextView = (TextView)layout.findViewById(R.id.frag_inf_telTextView);
        lunchtimeTextView = (TextView)layout.findViewById(R.id.frag_inf_lunchtimeTextView);
        addrTextView = (TextView)layout.findViewById(R.id.frag_inf_addrTextView);
        seatinfoTextView = (TextView)layout.findViewById(R.id.frag_inf_seatinfoTextView);
        resMenuImage = (ImageView) layout.findViewById(R.id.frag_inf_resMenuImage);

        telTextView.setText(restaurant.getMobile());
        lunchtimeTextView.setText(restaurant.getHours());
        Log.d(TAG, "onCreateView: " + DataUtils.hoursFormatter(restaurant.getHours()));
        addrTextView.setText(restaurant.getAddress());

        new RestaurantInfoRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/getRestInfo", Integer.toString(resNo));

        return layout;
    }

    /* 메뉴 하나와 현재 잔여좌석 */
    public class RestaurantInfoRequestTask extends AsyncTask<String, String, String> {
        String TAG = "RestaurantInfoRequestTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("resNo", strings[1]);
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
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d(TAG, "onPostExecute: "+ result);
            try {
                JSONObject jsonObject = new JSONObject(result);
                availablePlaceNum = jsonObject.getInt("s_left_num");
                seatinfoTextView.setText("현재 잔여 좌석: " + availablePlaceNum + "석");
                if (jsonObject.has("image")) {
                    image = jsonObject.getString("image");
                    JSONObject object = new JSONObject(image);
                    String data = object.getString("data");
                    String[] imageStrs = data.substring(1, data.length() - 1).split(",");
                    imageDatas = new byte[imageStrs.length];
                    for (int i = 0; i < imageStrs.length; i++) {
                        imageDatas[i] = DataUtils.intToByteArray(Integer.parseInt(imageStrs[i]));
                    }
                    Bitmap bmp = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
                    resMenuImage.setImageBitmap(bmp);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}


