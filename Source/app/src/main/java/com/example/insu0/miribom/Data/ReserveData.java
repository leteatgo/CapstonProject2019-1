package com.example.insu0.miribom.Data;

public class ReserveData {
    public String res_date;
    public int res_ppl_num;
    public String res_usrReq;
    public String res_time;


    public ReserveData(int pnum, int year, int month, int dayOfMonth, String usrReq, String time) {
        res_ppl_num = pnum;
        res_date = ((year) + "-" + (month+1) + "-" + (dayOfMonth));
        res_usrReq = usrReq;
        res_time = time;

    }

    public String getRes_date() {
        return res_date;
    }
    public String getRes_usrReq(){
        return res_usrReq;
    }
    public String getRes_time(){
        return res_time;
    }

    public int getRes_ppl_num() {
        return res_ppl_num;
    }
}
