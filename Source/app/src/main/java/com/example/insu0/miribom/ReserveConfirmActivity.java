package com.example.insu0.miribom;

import android.app.FragmentManager;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insu0.miribom.Lists.HomeRestaurantListItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//implemented by Insu Yang
public class ReserveConfirmActivity extends AppCompatActivity implements OnMapReadyCallback {

    /*GoogleMap Module*/
    private GoogleMap googleMap = null;
    private Marker currentMarker = null;
    private static final String TAG = "googlemap";

    /*Restaurant Info*/
    private HomeRestaurantListItem restaurant;
    private double lat;
    private double lon;
    private String restName;
    private String restAddr;
    private String restPhNum;


    /*Reservation Info*/
    private int personNum;
    private String resDate;
    private String resTime;

    /*View components*/
    private Button callBtn;
    private Button cancelBtn;
    private TextView personNumTV;
    private TextView resDateTV;
    private TextView resTimeTV;
    private ImageView resImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve_confirm);

        Bundle bundle = getIntent().getExtras();
        restaurant = bundle.getParcelable("ResItem");

        //resImageView 이미지 띄어줄 부분

        restName = restaurant.getResName();
        restAddr = restaurant.getAddress();
        restPhNum = restaurant.getMobile();
        lat = restaurant.getLatitude();
        lon = restaurant.getLongitude();

        Intent intent = getIntent();
        personNum = intent.getExtras().getInt("numofPerson");
        resTime = intent.getExtras().getString("res_time");
        resDate = intent.getExtras().getInt("res_year")+"/"+
                intent.getExtras().getInt("res_month")+"/"+
                intent.getExtras().getInt("res_dayOfMonth")+"";


        personNumTV = findViewById(R.id.resconf_personNum);
        personNumTV.setText(String.valueOf(personNum));
        resDateTV = findViewById(R.id.resconf_date);
        resDateTV.setText(String.valueOf(resDate));
        resTimeTV = findViewById(R.id.resconf_time);
        resTimeTV.setText(String.valueOf(resTime));


        callBtn = findViewById(R.id.resconf_callBtn);
        callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dialIntent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:" + restPhNum));
                startActivity(dialIntent);
            }
        });

        cancelBtn = findViewById(R.id.resconf_cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //여기 예약취소 로직 구현
                Toast.makeText(ReserveConfirmActivity.this, "예약 취소되었습니다", Toast.LENGTH_SHORT).show();
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment)fragmentManager
                .findFragmentById(R.id.resconf_map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng restaurantCoordinates = new LatLng(lat,lon);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(restaurantCoordinates);
        markerOptions.title(restName);
        markerOptions.snippet(restAddr);
        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(restaurantCoordinates));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(17));

    }
}
