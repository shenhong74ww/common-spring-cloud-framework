package com.middleware.common.util;

import org.springframework.util.StringUtils;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class StringUtil extends StringUtils {

    private static final String RANDOM_STRING_TEMPLATE = "23456789abcdefghjkmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";

    public static String generateRandomString(int length) {
        StringBuilder randomStrBuilder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int rand = (int) (Math.random() * RANDOM_STRING_TEMPLATE.length());
            randomStrBuilder.append(RANDOM_STRING_TEMPLATE.charAt(rand));
        }

        return randomStrBuilder.toString();
    }

    public static String md5Endcode(String source) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(source.getBytes());
            StringBuffer buffer = new StringBuffer();

            for (byte byteResult : result) {
                int number = byteResult & 0xff;
                String hexString = Integer.toHexString(number);

                if (hexString.length() == 1) {
                    buffer.append("0");
                }
                buffer.append(hexString);
            }

            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String base64Encode(String source) {
        return new BASE64Encoder().encode(source.getBytes());
    }
}
