package com.example.laxba.testapp.PaySig;

/**
 * Created by laxba on 11/15/2019.
 */
public class psUploadFile {

    private static String[] arrSigUploadFile;

    public psUploadFile() {
        arrSigUploadFile = new String[]{
                "<input type=\"file\"",};
    }

    public String[] getArrSigUploadFile() {
        return arrSigUploadFile;
    }
}