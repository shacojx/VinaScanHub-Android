package com.example.laxba.testapp.Scan;

import android.os.AsyncTask;
import android.util.Log;

import com.example.laxba.testapp.MainActivity;
import com.example.laxba.testapp.PaySig.psSQLi;
import com.example.laxba.testapp.function.EncodeValue;
import com.example.laxba.testapp.function.Scan;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by laxba on 11/25/2019.
 */
public class ScanXSS extends AsyncTask<Void, String, Void> {
    final String TAG = "TRUNGLB";
    private Element mElementScan;
    private String mURLScan;
    private String[] mPayloadScan;
    private HttpClient mClient;

    public ScanXSS() {
    }

    public ScanXSS(Element ElementScan, String URLScan, String[] PayloadScan, HttpClient Client) {
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
            scanXSS(mElementScan, mURLScan, mPayloadScan, mClient);
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
//        Log.d(TAG, "XONGGGGGGGGGGGGG: " + mURLScan);
    }

    private void scanXSS(final Element element, final String urlAction, final String[] payload, final HttpClient cClient) throws Exception {
        String vulnName = "XSS";
        String urlAttack = urlAction;
        boolean checkVuln = false;
        HttpClient client = new DefaultHttpClient();
        if (cClient != null){
            client = cClient;
        }
        List<NameValuePair> params;
        EncodeValue encodeValue = new EncodeValue();
        psSQLi psSQLi = new psSQLi();

        for (String sPay : payload) {
            String[] listPay = new String[0];
            try {
                listPay = new String[]{
                        encodeValue.decode(sPay),
                        sPay,
                        encodeValue.encode(sPay)
                };
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (String lPay : listPay) {
                params = new ArrayList<>();
                try {
                    if (element == null) {
                        try {
                            String fURL = urlAction.split("\\?")[0];
                            String lURL = urlAction.split("\\?")[1];
                            urlAttack = fURL;

                            for (String s : lURL.split("&")) {
                                final String key = s.split("\\=")[0];
                                String value = "";
                                try {
                                    value = s.split("\\=")[1] + lPay;
                                } catch (Exception e) {
                                    //System.out.println("Error Value attackVulnXSS: " + e);
                                }
                                final String tValue = value;
                                NameValuePair nameValuePair = new NameValuePair() {
                                    @Override
                                    public String getName() {
                                        return key;
                                    }

                                    @Override
                                    public String getValue() {
                                        return tValue;
                                    }
                                };
                                params.add(nameValuePair);
                            }
                        } catch (Exception e) {
                            //System.out.println("ERROR Case 1: " + e);
                        }
                    } else {
                        Elements ele = element.getElementsByAttribute("name");
                        for (Element e1 : ele) {
                            if (!e1.attr("type").toLowerCase().contains("submit") && !e1.attr("type").toLowerCase().contains("button")) {
                                final String sValue = lPay;
                                final String sName = e1.attr("name");
                                NameValuePair nameValuePair = new NameValuePair() {
                                    @Override
                                    public String getName() {
                                        return sName;
                                    }

                                    @Override
                                    public String getValue() {
                                        return sValue;
                                    }
                                };
                                params.add(nameValuePair);
                            } else {
                                if (e1.attr("name").length() != 0) {
                                    final String sValue = e1.attr("value");
                                    final String sName = e1.attr("name");
                                    NameValuePair nameValuePair = new NameValuePair() {
                                        @Override
                                        public String getName() {
                                            return sName;
                                        }

                                        @Override
                                        public String getValue() {
                                            return sValue;
                                        }
                                    };
                                    params.add(nameValuePair);
                                }
                            }
                        }
                    }
                    String method = "";
                    try {
                        method = element.attr("method");
                    } catch (Exception e) {
                    }
                    Scan scan = new Scan();
                    //
                    HttpGet httpGet = null;
                    HttpPost httpPost = null;
                    HttpResponse response;
                    if (method.toLowerCase().contains("post")) {
//                        requestSettings = new WebRequest(new URL(urlAction), HttpMethod.POST);
                        httpPost = new HttpPost(urlAction);
                        httpPost.setEntity(new UrlEncodedFormEntity(params));
                        response = client.execute(httpPost);
                        method = "|POST|";
                        scan.checkURLPOST.add(urlAction);
                    } else {
//                        requestSettings = new WebRequest(new URL(urlAttack), HttpMethod.GET);
                        String temp = urlAttack + "?";
                        for (NameValuePair pa : params) {
                            temp += pa.getName() + "=" + pa.getValue() + "&";
                        }
//                                Log.d(TAG, temp);
                        httpGet = new HttpGet(temp);
                        response = client.execute(httpGet);
                        method = "|GET|";
                        scan.checkURLGET.add(urlAttack);
                    }
                    HttpEntity entity = response.getEntity();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
                    String line = "";
                    String message = new String();
                    while ((line = rd.readLine()) != null) {
                        message += line;
                    }
                    String tempBody = message;

//                            Log.d(TAG, "1234        " + tempBody);
                    if (tempBody.replaceAll("\\s+", "").replace("//<![CDATA[", "").replace("//]]>", "").contains(encodeValue.decode(sPay))) {
                        boolean tempC = true;
                        for (String s : psSQLi.getArrSigSQLin()) {
                            if (tempBody.contains(s)) {
                                tempC = false;
                                break;
                            }
                        }
                        if (tempC) {
                            checkVuln = true;
                            Log.d(TAG, method + vulnName + " : " + urlAction);
                            final String tMethod = method;
                            final String tVulnName = vulnName;
                            MainActivity.actiApp.runOnUiThread(new Runnable() {
                                public void run() {
                                    //Do something on UiThread
                                    MainActivity.tv_tab2.append("\n----------\n");
                                    MainActivity.tv_tab1.append("\n----------\n");
                                    MainActivity.tv_tab2.append(tMethod + tVulnName + " : " + urlAction);
                                    MainActivity.tv_tab1.append(tMethod + tVulnName + " : " + urlAction);
                                }
                            });
                            for (NameValuePair s : params) {
                                Log.d(TAG, "      " + s.getName() + " = " + s.getValue());
                                final NameValuePair tS = s;
                                MainActivity.actiApp.runOnUiThread(new Runnable() {
                                    public void run() {
                                        //Do something on UiThread
                                        MainActivity.tv_tab2.append("\n     " + tS.getName() + " = " + tS.getValue());
                                        MainActivity.tv_tab1.append("\n     " + tS.getName() + " = " + tS.getValue());
                                    }
                                });
                            }
                            scan.list_vuln.add(method + vulnName + " : " + urlAction);
                            break;
                        }
                    }

                } catch (Exception e) {
                    Log.d(TAG, "Error attackVulnXSS: " + urlAction + " ||| " + e);
                }
            }
            if (checkVuln) {
                break;
            }
        }
        client.getConnectionManager().closeExpiredConnections();
        client.getConnectionManager().closeIdleConnections(0, TimeUnit.NANOSECONDS);
    }
}
