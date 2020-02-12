package com.example.laxba.testapp.function;

import android.os.AsyncTask;
import android.util.Log;

import com.example.laxba.testapp.MainActivity;
import com.example.laxba.testapp.Scan.ScanLFI;
import com.example.laxba.testapp.Scan.ScanSQLInjection;
import com.example.laxba.testapp.Scan.ScanXSS;
import com.example.laxba.testapp.Scan.Scan_LFI;
import com.example.laxba.testapp.Scan.Scan_SQLi;
import com.example.laxba.testapp.Scan.Scan_XSS;
import com.example.laxba.testapp.PaySig.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by laxba on 11/14/2019.
 */
public class Scan {
    final String TAG = "TRUNGLB";

    public Scan() {
    }

    public static ArrayList<String> list_vuln = new ArrayList<>();
    public static HashSet<String> checkURLGET = new HashSet<>();
    public static HashSet<String> checkURLPOST = new HashSet<>();

    //    Scan_BlindSQLi sBlinkSQLi = new Scan_BlindSQLi();
    Scan_SQLi sSQLi = new Scan_SQLi();
    //    Scan_XMLXpatchi sXMLXpatchi = new Scan_XMLXpatchi();
    Scan_XSS sXSS = new Scan_XSS();
    //    Scan_CodeInjection sCI = new Scan_CodeInjection();
    Scan_LFI sLFI = new Scan_LFI();
//    Scan_CMDinjection sCMDi = new Scan_CMDinjection();
//    Scan_WeakPassword sWeakPass = new Scan_WeakPassword();
//    Scan_DirList sDirList = new Scan_DirList();

    psSQLi psSQLin = new psSQLi();
    //    psXMLXpatchi psXMLXpatchin = new psXMLXpatchi();
    psXSS psXSS = new psXSS();
    //    psCodeInjection psCI = new psCodeInjection();
    psLFI psLFI = new psLFI();
//    psCMDInjection psCMDi = new psCMDInjection();
//    psUserPass psUP = new psUserPass();

    public void scanVuln(HashSet<String> listURL) throws Exception {
//        Log.d(TAG, "START SCAN!" + "SIZE: " + listURL.size());
        new ScanV().execute(listURL);
    }


    private class ScanV extends AsyncTask<HashSet<String>, Void, String> {
        @Override
        protected void onPreExecute() {
//            service = Executors.newFixedThreadPool(50);
        }

        @Override
        protected String doInBackground(HashSet<String>... hashSets) {
            for (HashSet<String> list : hashSets) {
                for (String sURL : list) {
                    if (!sURL.contains("png") && !sURL.contains("jpg")) {
                        if (sURL.contains("#")) {
                            sURL = sURL.split("#")[0];
                        }
                        if (sURL.contains("?") && sURL.contains("=")) {
                            if (!checkURLGET.contains(sURL.split("\\?")[0])) {
                                checkURLGET.add(sURL.split("\\?")[0]);
                                try {
                                    scanMethodGet(sURL);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        try {
                            Document document = Jsoup.connect(sURL).userAgent("Mozilla").followRedirects(false).get();
                            Elements linksOnPage = document.select("form");
                            for (Element element : linksOnPage) {
                                String temp = "";
                                try {
                                    temp = element.attr("abs:action");
                                } catch (Exception e) {
                                }

                                if (temp.length() == 0 || element.attr("action").equals("#")) {
                                    temp = sURL;
                                }

                                if (temp.contains("?") && temp.contains("=") && !checkURLGET.contains(temp.split("\\?")[0])) {
                                    checkURLGET.add(temp.split("\\?")[0]);
                                    scanMethodGet(temp);
                                }
                                String method = element.attr("method").toLowerCase();
                                if (method.contains("get") && !checkURLGET.contains(temp)) {
                                    checkURLGET.add(temp);
                                    scanMethodGetPost(element, temp);
                                } else {
                                    if (method.contains("post") && !checkURLPOST.contains(temp)) {
                                        checkURLPOST.add(temp);
                                        scanMethodGetPost(element, temp);
                                    }
                                }

                            }
                        } catch (Exception e) {
                            //System.out.println("Error scanVuln: " + sURL + " ||| " + e);
//                            e.printStackTrace();
                            Log.d(TAG, "ERROR SCAN: " + sURL + "     " + e);
                        }
                    }
                }
            }

//            service.shutdown();
//            try {
//                service.awaitTermination(1, TimeUnit.SECONDS);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            while (!service.isTerminated()) {
//                // Chờ xử lý hết các request còn chờ trong Queue ...
//            }
            return "SIZE: ";
        }

        @Override
        protected void onProgressUpdate(Void... voids) {
//            mProgressBar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d(TAG, "Scan end!");
            MainActivity.actiApp.runOnUiThread(new Runnable() {
                public void run() {
                    //Do something on UiThread
                    MainActivity.tv_tab2.append("\n----------\n");
                    MainActivity.tv_tab1.append("\n----------\n");
                    MainActivity.tv_tab1.append("Scan End!!!\n");
                }
            });
        }
    }


    private void scanMethodGet(final String urlAction) throws Exception {

//        sSQLi.scanSQLi(null, urlAction, psSQLin.getArrPaySQLin(), MainActivity.httpClient);

//
//                this.sXMLXpatchi.scanXMLXpatchin(null, urlAction, this.psXMLXpatchin.getArrPayXMLXPathin());

//        this.sXSS.scanXSS(null, urlAction, this.psXSS.getArrPayXSS(), MainActivity.httpClient);
//
//        this.sLFI.scanLFI(null, urlAction, this.psLFI.getArrPayLFI(), MainActivity.httpClient);
//
//                this.sCI.scanCI(null, urlAction, this.psCI.getArrPayCI());
//
//                this.sCMDi.scanCMDi(null, urlAction, this.psCMDi.getArrPayCMDi());


//        BlindSQLinjection(urlAction);


        ScanSQLInjection sqli = new ScanSQLInjection(null, urlAction, psSQLin.getArrPaySQLin(), MainActivity.httpClient);
        sqli.execute();
        ScanXSS xss = new ScanXSS(null, urlAction, psXSS.getArrPayXSS(), MainActivity.httpClient);
        xss.execute();
        ScanLFI lfi = new ScanLFI(null, urlAction, psLFI.getArrPayLFI(), MainActivity.httpClient);
        lfi.execute();
    }

    private void scanMethodGetPost(final Element element, final String urlAction) throws Exception {

//        sSQLi.scanSQLi(element, urlAction, psSQLin.getArrPaySQLin(), MainActivity.httpClient);


//
//
//                this.sXMLXpatchi.scanXMLXpatchin(element, urlAction, this.psXMLXpatchin.getArrPayXMLXPathin());
//

//        this.sXSS.scanXSS(element, urlAction, this.psXSS.getArrPayXSS(), MainActivity.httpClient);


//
//        this.sLFI.scanLFI(element, urlAction, this.psLFI.getArrPayLFI(), MainActivity.httpClient);
//
//
//
//                this.sCI.scanCI(element, urlAction, this.psCI.getArrPayCI());
//
//
//
//                this.sCMDi.scanCMDi(element, urlAction, this.psCMDi.getArrPayCMDi());

        ScanSQLInjection sqli = new ScanSQLInjection(element, urlAction, psSQLin.getArrPaySQLin(), MainActivity.httpClient);
        sqli.execute();
        ScanXSS xss = new ScanXSS(element, urlAction, psXSS.getArrPayXSS(), MainActivity.httpClient);
        xss.execute();
        ScanLFI lfi = new ScanLFI(element, urlAction, psLFI.getArrPayLFI(), MainActivity.httpClient);
        lfi.execute();
    }
}
