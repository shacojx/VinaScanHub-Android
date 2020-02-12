package com.example.laxba.testapp.Scan;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.example.laxba.testapp.MainActivity;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by laxba on 11/25/2019.
 */
public class Scan_Port extends AsyncTask<Void, Integer, Void> {

    Activity contextParent;
    public static ArrayList<Integer> ListPort = new ArrayList<>();
    public static String url = "";
    private final String TAG = "TRUNGLB";

    public Scan_Port(Activity contextParent) {
        this.contextParent = contextParent;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Hàm này sẽ chạy đầu tiên khi AsyncTask này được gọi
        //Ở đây mình sẽ thông báo quá trình load bắt đâu "Start"
        MainActivity.actiApp.runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                MainActivity.tv_tab1.append("Start Scan Port!!!\n");
            }
        });
    }

    @Override
    protected Void doInBackground(Void... params) {
        //Hàm được được hiện tiếp sau hàm onPreExecute()
        //Hàm này thực hiện các tác vụ chạy ngầm
        //Tuyệt đối k vẽ giao diện trong hàm này
        for (int port = 0; port <= 1000; port++) {
            try {
                Socket socket = new Socket();
                socket.connect(new InetSocketAddress(url, port), 1000);
                socket.close();
                ListPort.add(port);
                //khi gọi hàm này thì onProgressUpdate sẽ thực thi
                publishProgress(port);
            } catch (Exception e) {
//                e.printStackTrace();
                Log.d(TAG, "ERROR Scan Port: " + e);
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(final Integer... values) {
        //Hàm thực hiện update giao diện khi có dữ liệu từ hàm doInBackground gửi xuống
        super.onProgressUpdate(values);
        MainActivity.actiApp.runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                MainActivity.tv_tab1.append("Port " + values + " is open" + "\n");
            }
        });
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        //Hàm này được thực hiện khi tiến trình kết thúc
        //Ở đây mình thông báo là đã "Finshed" để người dùng biết
        MainActivity.actiApp.runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread
                MainActivity.tv_tab1.append("Scan Port End!!!\n");
            }
        });
    }
}
