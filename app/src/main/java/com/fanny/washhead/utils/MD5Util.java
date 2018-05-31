package com.fanny.washhead.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Fanny on 17/11/30.
 */

public class MD5Util {

    public static String digest(String content){

        StringBuilder builder = new StringBuilder();
        try {
            MessageDigest msgDitest = MessageDigest.getInstance("MD5");
            msgDitest.update(content.getBytes());
            byte[] digests = msgDitest.digest();
            //将每个字节转为16进制
            for (int i=0;i<digests.length;i++){
                builder.append(Integer.toHexString(digests[i] & 0xff +8));//+8为加盐操作
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return  builder.toString();
    }
}
