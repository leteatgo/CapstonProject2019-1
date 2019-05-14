package com.example.insu0.miribom.Servers;
import android.Manifest;

public interface MiribomInfo {
    String[] common_permissions = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.ACCESS_NETWORK_STATE };
    /*  setting server ip address   */
    String ipAddress = "35.243.118.35:5000";   //  Google Cloud Platform 외부 고정 ip
//    String ipAddress = "192.168.219.104:5000";   //  Local ip for Debugging

}