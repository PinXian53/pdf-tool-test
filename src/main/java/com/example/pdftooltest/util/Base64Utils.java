package com.example.pdftooltest.util;

import org.apache.tomcat.util.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class Base64Utils {

    private Base64Utils() {}

    public static String convertToBase64(byte[] file) {
        byte[] encoded = Base64.encodeBase64(file);
        return new String(encoded, StandardCharsets.UTF_8);
    }

    public static byte[] convertToBytes(String base64File) {
        return Base64.decodeBase64(base64File);
    }

}

