package com.example.laxba.testapp.Scan;

import android.os.AsyncTask;
import android.util.Log;

import com.example.laxba.testapp.MainActivity;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by laxba on 11/25/2019.
 */
public class ScanPort extends AsyncTask<Void, Void, Void> {
    //    private final ExecutorService pool;
    private String ipScan = "";
    private int sizePortScan = 0;
    private String TAG = "TRUNGLB";
    private boolean exists = false;


    public ScanPort(String url, int sizePort) {
        try {
            this.ipScan = new URL(url).getHost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.sizePortScan = sizePort;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"
        Log.d(TAG, "SCAN PORT START!!!");
        MainActivity.actiApp.runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                MainActivity.tv_tab1.append("SCAN PORT START!!!" + "\n");
            }
        });
    }

    protected Void doInBackground(Void... urls) {
        for (int i = 0; i <= sizePortScan; i++) {
            final int ttport = i;
            System.out.println("Port: " + ttport);
            new Thread(new Runnable() {
                public void run() {
                    try {
//                        InetAddress address = InetAddress.getByName("http://192.168.1.32:8080/dvwa/");
//                        String ipx = new URL("http://192.168.1.32:8080/dvwa/").getHost();
//                        System.out.println("PORT: " + port + "      IP: " + ipx);
                        SocketAddress sockaddr = new InetSocketAddress(ipScan, ttport);
                        // Create an unbound socket
                        Socket sock = new Socket();

                        // This method will block no more than timeoutMs.
                        // If the timeout occurs, SocketTimeoutException is thrown.
                        int timeoutMs = 1000;   // 2 seconds
                        sock.connect(sockaddr, timeoutMs);
                        exists = true;
                        if (exists) {
                            Log.d(TAG, "Port " + ttport + " is open" + "\n");
                            MainActivity.actiApp.runOnUiThread(new Runnable() {
                                public void run() {
                                    //Do something on UiThread
                                    MainActivity.tv_tab1.append("Port " + ttport + " is open" + "\n");
                                }
                            });
                            exists = false;
                        } else {
//                                System.out.println("DEOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: " + ttport);
                        }
                    } catch (Exception e) {
                        // Handle exception
                    }

                }
            }).start();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d(TAG, "SCAN PORT END!!!");
        MainActivity.actiApp.runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                MainActivity.tv_tab1.append("SCAN PORT END!!!" + "\n");
            }
        });
    }

}


