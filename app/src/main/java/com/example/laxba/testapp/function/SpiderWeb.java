package com.example.laxba.testapp.function;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by laxba on 11/14/2019.
 */
public class SpiderWeb{
//    public static int MAX_DEPTH = 5;
//    public static HashSet<String> links = new HashSet<>();
//    public static boolean checkSpider = false;
//    public static String TAG = "TrungLB";
//
//    public SpiderWeb() {
//    }
//
//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//    }
//
//    @Override
//    protected void onProgressUpdate() {
//        return;
//    }
//
//    @Override
//    protected void onPostExecute() {
//        return;
//    }
//
//    @Override
//    protected Object doInBackground() {
//        return null;
//    }
//
//    public SpiderWeb(HashSet<String> links) {
//        this.links = links;
//    }
//
//    public void getPageLinks(final String URL,final int depth, final String root_url) {
//
//        new Thread(new Runnable() {
//            public void run() {
//                try {
////                    Connection.Response resp = Jsoup.connect("https://daihoc.fpt.edu.vn/?s=%3Ch1%3ELow%3C%2Fh1%3E").userAgent("Mozilla").method(Connection.Method.GET).execute();
////                    Document doc = resp.parse();
//////                    Document doc = Jsoup.connect("https://daihoc.fpt.edu.vn/?s=%3Ch1%3ELow%3C%2Fh1%3E").get();
////                    Log.d("TrungLB", doc.body().toString());
//
//                    if ((!links.contains(URL) && (depth < MAX_DEPTH))
//                            && URL.contains(root_url)) {
//                        Log.d(TAG, ">> Depth: " + depth + " [" + URL + "]");
////                        VSH.LOG_CONSOLE.append(">> Depth: " + depth + " [" + URL + "]" + "\n");
////                        VSH.LOG_CONSOLE.setCaretPosition(VSH.LOG_CONSOLE.getDocument().getLength());
//                        try {
//                            links.add(URL);
//                            Document document = Jsoup.connect(URL).get();
//                            Elements linksOnPage = document.select("a[href]");
//
//                            for (Element page : linksOnPage) {
//                                getPageLinks(page.attr("abs:href"), depth + 1 , root_url);
//                            }
//
//                            String docString = document.body().toString();
//                            String regex = "[a-zA-Z0-9-_.]+@[a-zA-Z0-9-_.]+";
//
//                            Pattern pattern = Pattern.compile(regex);
//                            Matcher matcher = pattern.matcher(docString);
//
////                            while (matcher.find()) {
////                                String email = matcher.group();
////                                if (Param.listEmail.add(email)) {
////                                    System.out.println("Found 1 email: " + matcher.group());
//////                                    VSH.LOG_CONSOLE.append("Found 1 email: " + matcher.group() + "\n");
//////                                    VSH.LOG_CONSOLE.setCaretPosition(VSH.LOG_CONSOLE.getDocument().getLength());
////                                }
////                                // Get the group matched using group() method
////
////                            }
//                        } catch (Exception e) {
//                            Log.d("Error","For '" + URL + "': " + e.getMessage());
//                            e.printStackTrace();
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        Log.d(TAG, "checkSpider = "+ checkSpider);
//        Log.d(TAG, "SIZE:        " + links.size());
//        checkSpider = true;
//    }
}
