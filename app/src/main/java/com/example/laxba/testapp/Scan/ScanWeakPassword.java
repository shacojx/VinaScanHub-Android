package com.example.laxba.testapp.Scan;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by laxba on 11/27/2019.
 */
public class ScanWeakPassword extends AsyncTask<Void, String, Void> {
    final String TAG = "TRUNGLB";
    private Element mElementScan;
    private String mURLScan;
    private String[] mPayloadScan;
    private DefaultHttpClient mClient;
    private boolean checkLogin = false;

    public ScanWeakPassword() {
    }

    public ScanWeakPassword(Element ElementScan, String URLScan, String[] PayloadScan, DefaultHttpClient Client) {
        this.mElementScan = ElementScan;
        this.mURLScan = URLScan;
        this.mPayloadScan = PayloadScan;
        this.mClient = Client;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Hàm được được hiện tiếp sau hàm onPreExecute()
        //Hàm này thực hiện các tác vụ chạy ngầm
        //Tuyệt đối k vẽ giao diện trong hàm này
        try {

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        //Hàm thực hiện update giao diện khi có dữ liệu từ hàm doInBackground gửi xuống
        super.onProgressUpdate(values);
        //Thông qua contextCha để lấy được control trong MainActivity

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        Log.d(TAG, "XONGGGGGGGGGGGGG: " + mURLScan);
    }

    public void bruteForce(String sURL, String[][] userPass, CookieStore cookieStore) throws Exception {
//        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        /* turn off annoying htmlunit warnings */
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(java.util.logging.Level.OFF);
        DefaultHttpClient client = new DefaultHttpClient();
        if (cookieStore != null) {
            client.setCookieStore(cookieStore);
        }
        String action = "";
        HttpGet httpGet = new HttpGet(sURL);
        HttpResponse response = client.execute(httpGet);
        HttpEntity entity = response.getEntity();
        BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
        String line = "";
        String message = new String();
        while ((line = rd.readLine()) != null) {
            message += line;
        }
        String tempBody = message;
        Document doc = Jsoup.parse(tempBody);
        Elements allForm = doc.select("form");
        Element eFormLogin = null;
        String url1 = "";

        for (Element e : allForm) {
            String name1 = "";
            String id1 = "";
            try {
                url1 = e.attr("abs:action");
            } catch (Exception ex) {

            }
            try {
                name1 = e.attr("name");
            } catch (Exception ex) {

            }
            try {
                id1 = e.attr("id");
            } catch (Exception ex) {

            }
            if (url1.toLowerCase().contains("login")
                    || name1.toLowerCase().contains("login")
                    || id1.toLowerCase().contains("login")
                    || url1.toLowerCase().contains("dangnhap")
                    || name1.toLowerCase().contains("dangnhap")
                    || id1.toLowerCase().contains("dangnhap")) {
                eFormLogin = e;
                break;
            }
        }

        for (String[] uP : userPass) {
            checkPass(sURL, eFormLogin, uP, client);
            if (checkLogin) {
                break;
            }
        }
    }

    private void checkPass(String sURL, Element eFormLogin, String[] userPass, DefaultHttpClient client) throws Exception {
        String user = userPass[0];
        String pass = userPass[1];
        String urlAction = eFormLogin.attr("abs:action");
        EncodeString encode = new EncodeString();
        List<NameValuePair> params = null;
        List<NameValuePair> paramButton = null;

        Elements eles = eFormLogin.select("input, select, textarea, button");
//        Elements eles = eFormLogin.getElementsByAttribute("name");
        for (Element ele1 : eles) {
            String xname = ele1.attr("name");
            String xvalue = ele1.attr("value");
            String xtype = ele1.attr("type");
//                if (xname.length() != 0 && !name.contains(xname)) {
            if (xname.length() != 0) {
                if (xtype.contains("submit") || xtype.contains("button")) {
                    final String tValue = encode.encode(xvalue);
                    final String tKey = xname;
                    NameValuePair nameValuePair = new NameValuePair() {
                        @Override
                        public String getName() {
                            return tKey;
                        }

                        @Override
                        public String getValue() {
                            return tValue;
                        }
                    };
                    paramButton.add(nameValuePair);
                } else {
                    if (xvalue.length() != 0) {
                        final String tValue = encode.encode(xvalue);
                        final String tKey = xname;
                        NameValuePair nameValuePair = new NameValuePair() {
                            @Override
                            public String getName() {
                                return tKey;
                            }

                            @Override
                            public String getValue() {
                                return tValue;
                            }
                        };
                        params.add(nameValuePair);
                    } else {
                        if (xtype.contains("password")) {
                            final String tValue = encode.encode(pass);
                            final String tKey = xname;
                            NameValuePair nameValuePair = new NameValuePair() {
                                @Override
                                public String getName() {
                                    return tKey;
                                }

                                @Override
                                public String getValue() {
                                    return tValue;
                                }
                            };
                            params.add(nameValuePair);
                        } else {
                            final String tValue = encode.encode(user);
                            final String tKey = xname;
                            NameValuePair nameValuePair = new NameValuePair() {
                                @Override
                                public String getName() {
                                    return tKey;
                                }

                                @Override
                                public String getValue() {
                                    return tValue;
                                }
                            };
                            params.add(nameValuePair);
                        }
                    }
                }
            }
        }

        String method = "get";
        try {
            method = eFormLogin.attr("method");
        } catch (Exception e) {
//                            e.printStackTrace();
        }
        HttpGet httpGet = null;
        HttpPost httpPost = null;
        HttpResponse response;
        if (method.toLowerCase().contains("post")) {
            httpPost = new HttpPost(urlAction);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            response = client.execute(httpPost);
            method = "|POST|";
//            scan.checkURLPOST.add(urlAction);
        } else {
            String temp = urlAction + "?";
            for (NameValuePair pa : params) {
                temp += pa.getName() + "=" + pa.getValue() + "&";
            }
//                                Log.d(TAG, temp);
            httpGet = new HttpGet(temp);
//                            Log.d(TAG, "Body " + temp);
            response = client.execute(httpGet);
            method = "|GET|";
//            scan.checkURLGET.add(urlAttack);
        }
        HttpEntity entity = response.getEntity();
        String pURL = response.getLastHeader("Location").getValue();
        BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
        String line = "";
        String message = new String();
        while ((line = rd.readLine()) != null) {
            message += line;
        }
        String tempBody = message;
        Document doc = Jsoup.parse(tempBody);
        Elements allForm = doc.select("form");

        boolean checkLogin1 = false;
        boolean checkLogin2 = true;
        boolean checkLogin3 = false;
        if (urlAction.contains(sURL)) {
            checkLogin3 = true;
        }
        for (Element f : allForm) {
//            System.out.println("Form: " + f.getActionAttribute());
            if (checkLogin3) {
                checkLogin1 = true;
                if (f.attr("abs:action").contains(urlAction)) {
                    checkLogin1 = false;
                    break;
                }
            } else {
                if (pURL.contains(urlAction)) {
                    checkLogin1 = true;
                    break;
                }
            }
        }
        if (allForm.size() == 0) {
            checkLogin1 = true;
            checkLogin2 = true;
        }
        if (checkLogin1 && checkLogin2) {
            System.out.println("Login Thanh Cong : " + sURL);
            System.out.println("User: " + user + " ---- Password: " + pass);
        }
    }
}