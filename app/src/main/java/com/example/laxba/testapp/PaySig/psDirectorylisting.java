package com.example.laxba.testapp.PaySig;

/**
 * Created by laxba on 11/15/2019.
 */
public class psDirectorylisting {
    private static String[] arrSigCI;

    public psDirectorylisting() {
        arrSigCI = new String[]{
                "<title>Index of /",
                "<h1>Index of /",
                "Index of /",
        };
    }


    public String[] getArrSigDL() {
        return arrSigCI;
    }

}
