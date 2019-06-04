package com.example.insu0.miribom.Data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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

public class DataUtils {
    public static String TAG = "DATAUTILS";
    public static byte intToByteArray(int a)
    {
        return (byte) (a & 0xFF);
    }

    public static String hoursFormatter(String hours) {
        String str = null;
        try {
            String s = hours.replace("\\","");
//            s = s.substring(1, s.length()-1);
            Log.d(TAG, "hoursFormatter: " + s);
            JSONObject jsonObject = new JSONObject(s);
            int type = jsonObject.getInt("t");
            String[] bStr;
            String[] hStr;
            JSONArray hJson;
            String closedStr;
            switch (type) {
                case 1: // 쉬는날 o break o 모든 시간 동일
                    closedStr = jsonObject.getString("cd");
                    bStr = jsonObject.getString("break").split("~");
                    hStr = jsonObject.getString("time").split("~");
                    str = "매일 " + hStr[0] + "~" + bStr[0] + "\n" +
                            bStr[1] + "~" + hStr[1] + "\n" + getClosedDayInfo(closedStr);
                    break;
                case 2: // 쉬는날 o break x 모든 시간 동일
                    closedStr = jsonObject.getString("cd");
                    str = "매일" + jsonObject.getString("time")
                    + "\n" + getClosedDayInfo(closedStr);
                    break;
                case 3: // 쉬는날 x break o 모든 시간 동일
                    bStr = jsonObject.getString("break").split("~");
                    hStr = jsonObject.getString("time").split("~");
                    str = "매일 " + hStr[0] + "~" + bStr[0] + "\n" +
                            bStr[1] + "~" + hStr[1];
                    break;
                case 4: // 쉬는날 x break x 모든 시간 동일
                    str = "매일 " + jsonObject.getString("time");
                    break;
                case 5: // 쉬는날 o break o 평일/주말
                    bStr = jsonObject.getString("break").split("~");
                    hJson = jsonObject.getJSONArray("time");
                    str = "평일 " + "\n" +
                            "주말 ";
                    break;
                case 6: // 쉬는날 o break x 평일/주말
                    hJson = jsonObject.getJSONArray("time");
                    str = "평일 " + "\n" +
                            "주말 ";
                    break;
                case 7: // 쉬는날 x break o 평일/주말
                    bStr = jsonObject.getString("break").split("~");
                    hJson = jsonObject.getJSONArray("time");
                    str = "평일 " + "\n" +
                            "주말 ";
                    break;
                case 8: // 쉬는날 x break x 평일/주말
                    hJson = jsonObject.getJSONArray("time");
                    str = "평일 " + "\n" +
                            "주말 ";
                    break;
                case 9: // 쉬는날 o break o 다 다름
                    bStr = jsonObject.getString("break").split("~");
                    hJson = jsonObject.getJSONArray("time");

                    break;
                case 10: // 쉬는날 o break x 다 다름
                    hJson = jsonObject.getJSONArray("time");
                    break;
                case 11: // 쉬는날 x break o 다 다름
                    bStr = jsonObject.getString("break").split("~");
                    hJson = jsonObject.getJSONArray("time");
                    break;
                case 12: // 쉬는날 x break x 다 다름
                    hJson = jsonObject.getJSONArray("time");
                    break;

            }

        } catch (JSONException e) {
            Log.d(TAG, "hoursFormatter: json error");
            e.printStackTrace();
        }
        Log.d(TAG, "hoursFormatter: " + str);
        return str;
    }

    private static String getParsedHours(JSONObject time) {
        StringBuilder sb = new StringBuilder();

        return sb.toString();
    }
    private static String getClosedDayInfo(String cd) {
        StringBuilder sb = new StringBuilder();
        String[] cds = cd.split(",");
        for (int i = 0; i < cds.length; i++) {
            switch (Integer.parseInt(cds[i])) {
                case 0:
                    sb.append("월");
                    break;
                case 1:
                    sb.append("화");
                    break;
                case 2:
                    sb.append("수");
                    break;
                case 3:
                    sb.append("목");
                    break;
                case 4:
                    sb.append("금");
                    break;
                case 5:
                    sb.append("토");
                    break;
                case 6:
                    sb.append("일");
                    break;
            }
            if (i == cds.length - 1)
                break;
            sb.append(", ");
        }
        sb.append("요일 휴무");
        return sb.toString();
    }

//    public class ImageLoader extends AsyncTask<String, String, String> {
//        String TAG = "ImageLoader>>>";
//
//        private Bitmap bitmap;
//
//        @Override
//        protected String doInBackground(String... strings) {
//            JSONObject reqInfo = new JSONObject();
//            try {
//                reqInfo.accumulate("imageUrl", strings[1]);
//                HttpURLConnection conn = null;
//                BufferedReader reader = null;
//                URL url = new URL(strings[0]);
//                // settings
//                conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("POST");
//                conn.setRequestProperty("Cache-Control", "no-cache");
//                conn.setRequestProperty("Content-Type", "application/json");
//                conn.setRequestProperty("Accept", "application/text");
//                conn.setRequestProperty("Accept", "application/json");
//                conn.setDoOutput(true);
//                conn.setDoInput(true);
//                conn.connect();
//
//                OutputStream outputStream = conn.getOutputStream();
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream));
//                writer.write(reqInfo.toString());
//                writer.flush();
//                writer.close();
//
//                InputStream stream = conn.getInputStream();
//                reader = new BufferedReader(new InputStreamReader(stream));
//                StringBuffer buffer = new StringBuffer();
//                String line = "";
//                while ((line = reader.readLine()) != null) {
//                    buffer.append(line);
//                    Log.d(TAG, "doInBackground: readLine, " + line);
//                }
//                return buffer.toString();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            JSONObject jsonObject = null;
//            try {
//                jsonObject = new JSONObject(result);
//                String imageStr = jsonObject.getString("image");
//                JSONObject imageObject = new JSONObject(imageStr);
//                String data = imageObject.getString("data");
//                String[] imageStrs = data.substring(1, data.length() - 1).split(",");
//                byte[] imageDatas = new byte[imageStrs.length];
//                for (int j = 0; j < imageStrs.length; j++) {
//                    imageDatas[j] = DataUtils.intToByteArray(Integer.parseInt(imageStrs[j]));
//                }
//                bitmap = BitmapFactory.decodeByteArray(imageDatas, 0, imageDatas.length);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public Bitmap getBitmap() {
//            return bitmap;
//        }
//    }
}
