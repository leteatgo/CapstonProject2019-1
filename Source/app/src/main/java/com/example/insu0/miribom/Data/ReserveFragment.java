package com.example.insu0.miribom.Data;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.insu0.miribom.HomeActivity;
import com.example.insu0.miribom.Lists.DateList;
import com.example.insu0.miribom.Lists.DateListAdapter;
import com.example.insu0.miribom.R;
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
import java.net.ContentHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

@SuppressLint("ValidFragment")
public class ReserveFragment extends Fragment implements View.OnClickListener{
    private String TAG = "ReserveFragment";
    private int uNo, resNo, availablePlaceNum;
    private Context context;
    private int numofPerson;

    private ArrayList<String> notAvaTime;
    private StringBuilder sb = new StringBuilder();
    private ArrayList<String> itemList;

    private RecyclerView dateListView;
    private DateListAdapter dateListAdapter;

    private Button res_Btn;


    /*ReserveData*/
    private int res_ppl_num;
    private int res_year;
    private int res_month;
    private int res_dayOfMonth;
    private String res_usrReq;
    private String res_time;



    public ReserveFragment(int uNo, int resNo, int availablePlaceNum, Context context) {
        this.uNo = uNo;
        this.resNo = resNo;
        this.availablePlaceNum = availablePlaceNum;
        this.context = context;
    }

    public ReserveData sendData(){
        ReserveData sendingData = new ReserveData(res_ppl_num,res_year,res_month,res_dayOfMonth,res_usrReq,res_time);
        return sendingData;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("WrongViewCast")
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_res_reserve,container,false);

        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_res_reserve, container, false);
        dateListView = (RecyclerView) rootView.findViewById(R.id.time_chooser_listview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        dateListView.setLayoutManager(layoutManager);
        dateListView.setHasFixedSize(true);
        setDateListTest();

        NumberPicker picker = (NumberPicker)rootView.findViewById(R.id.number);
        picker.setMinValue(1);
        picker.setMaxValue(10); //이거는 추후에 예약 프로그램에서 받아오는 값으로 대체 필요하다
        numofPerson = picker.getValue();
        res_ppl_num = numofPerson;


        CalendarView calendarview = (CalendarView)rootView.findViewById(R.id.frag_res_calendar);
        calendarview.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {

                res_year = year;
                res_month = month;
                res_dayOfMonth = dayOfMonth;

                /* 여기 안에 날짜정보를 서버에 보내는걸 IMPLEMENT*/
                setDateList(""+year+"/"+(month+1)+"/"+dayOfMonth);
                Toast.makeText(getActivity(), ""+year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_LONG).show();
            }
        });

        EditText userRequest = (EditText)rootView.findViewById(R.id.userrequest);
        res_usrReq = String.valueOf(userRequest.getText());


        Button res_Btn = (Button)rootView.findViewById(R.id.resbutton);
        res_Btn.setOnClickListener(this);
        return rootView;
    }

    private void setDateListTest(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd");
        String getTime = sdf.format(date);
        Log.d(TAG, "setDateListTest: " + getTime);
        itemList = new ArrayList<String>();

        itemList.add("11:00");
        itemList.add("11:30");
        itemList.add("12:00");
        itemList.add("12:30");
        itemList.add("13:00");
        itemList.add("13:30");
        itemList.add("14:00");
        itemList.add("14:30");
        itemList.add("15:00");
        itemList.add("15:30");
        itemList.add("16:00");
        itemList.add("16:30");
        itemList.add("17:00");
        itemList.add("17:30");
        itemList.add("18:00");
        itemList.add("18:30");
        itemList.add("19:00");
        itemList.add("19:30");
        itemList.add("20:00");
        itemList.add("20:30");
        itemList.add("21:00");
        itemList.add("21:30");
        itemList.add("22:00");
        itemList.add("22:30");
        itemList.add("23:00");
        itemList.add("23:30");


        new FindRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/reservation/find", Integer.toString(resNo)
                ,getTime);

        dateListAdapter = new DateListAdapter(getActivity(),itemList,onClickItem);
        dateListView.setAdapter(dateListAdapter);
        DateList dateList = new DateList();
        dateListView.addItemDecoration(dateList);
    }

    private void setDateList(String date) {
        itemList = new ArrayList<String>();

        itemList.add("11:00");
        itemList.add("11:30");
        itemList.add("12:00");
        itemList.add("12:30");
        itemList.add("13:00");
        itemList.add("13:30");
        itemList.add("14:00");
        itemList.add("14:30");
        itemList.add("15:00");
        itemList.add("15:30");
        itemList.add("16:00");
        itemList.add("16:30");
        itemList.add("17:00");
        itemList.add("17:30");
        itemList.add("18:00");
        itemList.add("18:30");
        itemList.add("19:00");
        itemList.add("19:30");
        itemList.add("20:00");
        itemList.add("20:30");
        itemList.add("21:00");
        itemList.add("21:30");
        itemList.add("22:00");
        itemList.add("22:30");
        itemList.add("23:00");
        itemList.add("23:30");

        new FindRequestTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/reservation/find", Integer.toString(resNo)
                ,date);
        dateListAdapter = new DateListAdapter(getActivity(),itemList,onClickItem);
        dateListView.setAdapter(dateListAdapter);
        DateList dateList = new DateList();
        dateListView.addItemDecoration(dateList);
    }

    private View.OnClickListener onClickItem = new View.OnClickListener(){
        @Override
        public void onClick(View v) {

            String str = (String) v.getTag();
            res_time =str;
            Toast.makeText(getActivity(),str,Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resbutton:
                ReserveData reserveData = sendData();
                /*Server로 보낸다*/
                /*get 함수 쓰세요 ReserveData꺼*/
                new MakeReservationTask().execute("http://" + MiribomInfo.ipAddress + "/restaurant/reservation/make",
                        Integer.toString(uNo) ,Integer.toString(resNo), reserveData.getRes_date(), reserveData.getRes_time()+":00", Integer.toString(5000),
                        Integer.toString(reserveData.getRes_ppl_num()), reserveData.getRes_usrReq());
//                Toast.makeText(context,reserveData.getRes_date() , Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public class MakeReservationTask extends AsyncTask<String, String, String> {
        String TAG = "MakeReservationTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("uNo", strings[1]);
                reqInfo.accumulate("resNo", strings[2]);
                reqInfo.accumulate("date", strings[3]);
                reqInfo.accumulate("time", strings[4]);
                reqInfo.accumulate("deposit", strings[5]);
                reqInfo.accumulate("peopleNum", strings[6]);
                reqInfo.accumulate("userRequest", strings[7]);

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
            // 예약되었습니다. or 예약 등록에 실패 하였습니다.
            Uri kakaopayurl = Uri.parse(result);
            Intent gotokakao = new Intent(Intent.ACTION_VIEW, kakaopayurl);
            startActivity(gotokakao);
            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    }

    public class FindRequestTask extends AsyncTask<String, String, String> {
        String TAG = "FindRequestTask>>>";

        @Override
        protected String doInBackground(String... strings) {
            try {
                JSONObject reqInfo = new JSONObject();
                reqInfo.accumulate("resNo", strings[1]);
                reqInfo.accumulate("date", strings[2]);
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
                JSONArray array = new JSONArray(result);
                HashMap<String, Integer> map = new HashMap<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    String time = jsonObject.getString("time");
                    if (map.containsKey(time)) {
                        int count = map.get(time);
                        map.put(time, count + 1);
                    } else {
                        map.put(time, 1);
                    }
                }
                Set<String> keySet = map.keySet();
                Iterator<String> iterator = keySet.iterator();
                notAvaTime = new ArrayList<>();
                while(iterator.hasNext()) {
                    String key = iterator.next();
                    if (map.get(key) >= availablePlaceNum)
                        notAvaTime.add(key);
                }
                for (int i = 0; i < notAvaTime.size(); i++) {
                    String time = notAvaTime.get(i);
                    itemList.remove(time.substring(0, time.length()-3));
                }
                Log.d(TAG, "onPostExecute: " + notAvaTime.toString());

                dateListAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(context, "실패", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}

