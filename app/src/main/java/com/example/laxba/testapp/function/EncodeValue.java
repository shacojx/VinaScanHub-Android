package com.example.laxba.testapp.function;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by laxba on 11/15/2019.
 */
public class EncodeValue {
    public EncodeValue() {
    }

    // Method to encode a string value using `UTF-8` encoding scheme
    public String encode(String value) throws UnsupportedEncodingException {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    // Decodes a URL encoded string using `UTF-8`
    public String decode(String value) throws UnsupportedEncodingException {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
