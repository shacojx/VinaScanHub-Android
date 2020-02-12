package com.example.laxba.testapp.Scan;

import com.example.laxba.testapp.function.Scan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by laxba on 11/15/2019.
 */
public class Scan_BlindSQLi {

    public Scan_BlindSQLi() {
    }


    public void BlindSQLinjection(String urlAction) throws Exception {
        Scan s = new Scan();
        try {
            Document doc = Jsoup.connect(urlAction).get();
            Document doc1 = Jsoup.connect(urlAction + " and 1=1").get();
            Document doc2 = Jsoup.connect(urlAction + " and 1=2").get();
            Document doc3 = Jsoup.connect(urlAction + "' and 1=2").get();
            Document doc4 = Jsoup.connect(urlAction + "' and 1=2").get();
            if (doc.toString().equals(doc1.toString()) == true && doc.toString().equals(doc2.toString()) == false) {
                if (s.list_vuln.size() != 0) {
                    for (String x : s.list_vuln) {
                        if (!x.contains(urlAction.split("=")[0])) {
                            s.list_vuln.add("Get Blind SQL injection: " + urlAction);
                        }
                    }
                } else {
                    s.list_vuln.add("Get Blind SQL injection: " + urlAction);
                }
            }
        } catch (Exception e) {
            System.out.println("Error Get Blind SQL injection: " + urlAction + "Ex: " + e);
        }

    }
}
