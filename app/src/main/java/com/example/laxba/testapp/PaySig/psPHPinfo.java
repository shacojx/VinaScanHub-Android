package com.example.laxba.testapp.PaySig;

/**
 * Created by laxba on 11/15/2019.
 */
public class psPHPinfo {
    private static String[] arrSigPHPinfo;

    public psPHPinfo() {
        arrSigPHPinfo = new String[]{
                "<title>phpinfo()</title>",
        };

    }


    public String[] getArrSigPHPinfo() {
        return arrSigPHPinfo;
    }
}
