package com.example.laxba.testapp.Scan;

import android.util.Log;

import com.example.laxba.testapp.MainActivity;
import com.example.laxba.testapp.PaySig.psLFI;
import com.example.laxba.testapp.PaySig.psSQLi;
import com.example.laxba.testapp.function.EncodeValue;
import com.example.laxba.testapp.function.Scan;

import org.apache.commons.logging.LogFactory;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by laxba on 11/25/2019.
 */
public class Scan_LFI {
    final String TAG = "TRUNGLB";

    public Scan_LFI() {
    }

    public void scanLFI(final Element element, final String urlAction, final String[] payload, final HttpClient cClient) throws Exception {
        LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        String vulnName = "LFI";
        String urlAttack = urlAction;
        boolean checkVuln = false;
        HttpClient client = new DefaultHttpClient();
        if (cClient != null) {
            client = cClient;
        }
        List<NameValuePair> params;
        EncodeValue encodeValue = new EncodeValue();
        psLFI psLFI = new psLFI();

        for (String sPay : payload) {
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
                                value = s.split("\\=")[1] + sPay;
                            } catch (Exception e) {
                                e.printStackTrace();
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
                        e.printStackTrace();
                    }
                } else {
                    Elements ele = element.getElementsByAttribute("name");
                    for (Element e1 : ele) {
                        if (!e1.attr("type").toLowerCase().contains("submit") && !e1.attr("type").toLowerCase().contains("button")) {
                            final String tValue = sPay;
                            final String tKey = e1.attr("name");
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
                            if (e1.attr("name").length() != 0) {
                                final String tValue = e1.attr("value");
                                final String tKey = e1.attr("name");
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
                String method = "";
                try {
                    method = element.attr("method");
                } catch (Exception e) {
//                            e.printStackTrace();
                }
                Scan scan = new Scan();
                HttpGet httpGet = null;
                HttpPost httpPost = null;
                HttpResponse response;
                if (method.toLowerCase().contains("post")) {
                    httpPost = new HttpPost(urlAction);
                    httpPost.setEntity(new UrlEncodedFormEntity(params));
                    response = client.execute(httpPost);
                    method = "|POST|";
                    scan.checkURLPOST.add(urlAction);
                } else {
                    String temp = urlAttack + "?";
                    for (NameValuePair pa : params) {
                        temp += pa.getName() + "=" + pa.getValue() + "&";
                    }
//                                Log.d(TAG, temp);
                    httpGet = new HttpGet(temp);
//                            Log.d(TAG, "Body " + temp);
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
//                        Log.d(TAG, "Body " + tempBody);

                for (String sSig : psLFI.getArrSigLFI()) {
                    if (tempBody.contains(sSig)) {
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
                Log.d(TAG, "Error attackVulnLFI: " + urlAction + " ||| " + e);
            }
            if (checkVuln) {
                break;
            }
        }
        client.getConnectionManager().closeExpiredConnections();
        client.getConnectionManager().closeIdleConnections(0, TimeUnit.NANOSECONDS);
    }
}
