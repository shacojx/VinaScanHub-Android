package com.example.laxba.testapp;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.laxba.testapp.Scan.ScanPort;
import com.example.laxba.testapp.Scan.Scan_Port;
import com.example.laxba.testapp.function.Scan;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
//import org.jsoup.Connection;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText mURL;
    private Button mStart;
    private Button mPort;
    public static HashSet<String> links = new HashSet<>();
    final static String TAG = "TRUNGLB";
    static String URLScan;
    public static TextView tv_tab1;
    public static TextView tv_tab2;
    public static Activity actiApp;
    private int maxDept;
    public static HttpClient httpClient = null;
    Scan_Port mScanPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        actiApp = MainActivity.this;
        mURL = (EditText) findViewById(R.id.edit_url);
        mStart = (Button) findViewById(R.id.btn_Start);
        mPort = (Button) findViewById(R.id.btn_Port);
        maxDept = Integer.parseInt(((EditText) findViewById(R.id.edit_dept)).getText().toString());
        mStart.setOnClickListener(this);
        mPort.setOnClickListener(this);

        loadTabs();

        tv_tab1 = (TextView) findViewById(R.id.tv_log);
        tv_tab1.setMovementMethod(new ScrollingMovementMethod());
        tv_tab1.setScrollbarFadingEnabled(false);
        tv_tab2 = (TextView) findViewById(R.id.tv_vuln);
        tv_tab2.setMovementMethod(new ScrollingMovementMethod());

//        new Thread(new Runnable() {
//            public void run() {
//                try {
//                    HttpClient client = new DefaultHttpClient();
//
//                    HttpGet request = new HttpGet("https://daihoc.fpt.edu.vn/?s=%3Ch1%3ELow%3C%2Fh1%3E");
//                    HttpResponse response = client.execute(request);
//// Get the response
//                    HttpEntity entity = response.getEntity();
//                    BufferedReader rd = new BufferedReader(new InputStreamReader(entity.getContent()));
//                    String line = "";
//                    String message = new String();
//                    while ((line = rd.readLine()) != null) {
//                        if (line.contains("&lt;h1&gt;Low&lt;/h1&gt;")) {
//                            Log.d(TAG, "Kết quả điện thoại:  " + line);
//                        }
//                        message += line;
//                    }
//                    Document doc = Jsoup.parse(message, "", Parser.xmlParser());
//                    Log.d(TAG, doc.body().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//        new Thread(new Runnable() {
//            public void run() {
//                try {
//            Connection.Response resp = Jsoup.connect("https://daihoc.fpt.edu.vn/?s=%3Ch1%3ELow%3C%2Fh1%3E").userAgent("Mozilla").method(Connection.Method.GET).execute();
//            Document doc = resp.parse();
////                    Document doc = Jsoup.connect("https://daihoc.fpt.edu.vn/?s=%3Ch1%3ELow%3C%2Fh1%3E").get();
//                    Log.d("TrungLB", doc.body().toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    public void getPageLinks(final String URL, final int depth, final String root_url) {
        if ((!links.contains(URL) && (depth <= maxDept)) && (URL.contains(root_url))) {
            Log.d(TAG, ">> Depth: " + depth + " [" + URL + "]");
            final String tURL = URL;
            runOnUiThread(new Runnable() {
                public void run() {
                    //Do something on UiThread
                    tv_tab1.append(">> Depth: " + depth + " [" + tURL + "]\n");
                }
            });
            try {
                links.add(URL);
                Document document = Jsoup.connect(URL).get();
                Elements linksOnPage = document.select("a[href]");

//                depth++;
                final int levelDept = depth + 1;

                for (Element page : linksOnPage) {
                    final String tempURL = page.attr("abs:href");
                    getPageLinks(tempURL, levelDept, root_url);
                }
            } catch (Exception e) {
                Log.d(TAG, "For '" + URL + "': " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    static HashSet<String> xlistScan = new HashSet<>();
    static HashSet<String> xlistURL = new HashSet<>();

    //https://henhoketban.vn
    //http://testphp.vulnweb.com/
    //http://192.168.1.32:8080/dvwa/index.php
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Start:
                new ScanVulnURL().execute();
                break;
            case R.id.btn_Port:
                //Khởi tạo tiến trình của bạn
                ScanPort mScanPort = new ScanPort(mURL.getText().toString(), 10000);
                //Gọi hàm execute để kích hoạt tiến trình
                mScanPort.execute();
                break;
            default:
                break;
        }
    }


    private class ScanVulnURL extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            URLScan = mURL.getText().toString();
            if (URLScan.contains("dvwa")) {
                httpClient = new DefaultHttpClient();
                HttpGet httpGet = null;
                httpGet = new HttpGet(URLScan);
                HttpResponse response;
                try {
                    response = httpClient.execute(httpGet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            runOnUiThread(new Runnable() {
                public void run() {
                    //Do something on UiThread
                    tv_tab1.append("Start Scan!!!\n");
                    tv_tab1.append("Start Spider Level: " + maxDept + "\n");
                }
            });
        }

        @Override
        protected String doInBackground(Void... voids) {
            Log.d(TAG, "Start Scan!!!\n");
            Log.d(TAG, "Start Spider Level: " + maxDept);
            getPageLinks(URLScan, 1, URLScan.split("://")[1]);
            return "" + links.size();
        }

        @Override
        protected void onPostExecute(String result) {
            final String tResult = result;
            Log.d(TAG, "Done Spider!!!");
            Log.d(TAG, "Total link spider: " + tResult);
            Log.d(TAG, "Scan Vuln!!!");
            runOnUiThread(new Runnable() {
                public void run() {
                    //Do something on UiThread
                    tv_tab1.append("Done Spider!!!\n");
                    tv_tab1.append("Total link spider: " + tResult + "\n");
                    tv_tab1.append("Scan Vuln!!!\n");
                }
            });

            Scan ss = new Scan();
            try {
                ss.scanVuln(links);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Cấu hình tab
    public void loadTabs() {
        //Lấy Tabhost id ra trước (cái này của built - in android
        final TabHost tab = (TabHost) findViewById
                (android.R.id.tabhost);
        //gọi lệnh setup
        tab.setup();
        TabHost.TabSpec spec;
        //Tạo tab1
        spec = tab.newTabSpec("t1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("1-LOG");
        tab.addTab(spec);
        //Tạo tab2
        spec = tab.newTabSpec("t2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("2-VULN");
        tab.addTab(spec);
        //Thiết lập tab mặc định được chọn ban đầu là tab 0
        tab.setCurrentTab(0);
        //Ở đây Tôi để sự kiện này để các bạn tùy xử lý
        //Ví dụ tab1 chưa nhập thông tin xong mà lại qua tab 2 thì báo...
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            public void onTabChanged(String arg0) {
                String s = "Tab tag =" + arg0 + "; index =" + tab.getCurrentTab();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }
        });
    }


    private class DownloadFilesTask extends AsyncTask<Void, Integer, Long> {
        boolean exists = false;
        private int port;

        public DownloadFilesTask(int port) {
            this.port = port;
        }

        protected Long doInBackground(Void... urls) {
            long totalSize = 0;
            final int ttport = this.port;
            new Thread(new Runnable() {
                public void run() {

                    try {

//                        InetAddress address = InetAddress.getByName("http://192.168.1.32:8080/dvwa/");
                        String ipx = new URL("http://192.168.1.20:8080/dvwa/").getHost();
                        System.out.println("PORT: "+port+"      IP: "+ipx);
                        SocketAddress sockaddr = new InetSocketAddress(ipx, ttport);
                        // Create an unbound socket
                        Socket sock = new Socket();

                        // This method will block no more than timeoutMs.
                        // If the timeout occurs, SocketTimeoutException is thrown.
                        int timeoutMs = 1000;   // 2 seconds
                        sock.connect(sockaddr, timeoutMs);
                        exists = true;
                        if (exists) {
                            System.out.println("OPENNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN: " + ttport);
                            exists = false;
                        } else {
                            System.out.println("DEOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO: " + ttport);
                        }
                    } catch (IOException e) {
                        // Handle exception
                    }

                }
            }).start();
            return totalSize;
        }
    }
}
