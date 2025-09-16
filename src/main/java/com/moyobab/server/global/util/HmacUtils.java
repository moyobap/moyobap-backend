package com.moyobab.server.global.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;

public class HmacUtils {

    private static final String HMAC_ALGO = "HmacSHA256";

    public static String hmacSha256(String secret, String data) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMAC_ALGO);
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(hmacBytes);
        } catch (Exception e) {
            throw new RuntimeException("HMAC 생성 실패", e);
        }
    }

    public static boolean isEqual(String hmac1, String hmac2) {
        return MessageDigest.isEqual(hmac1.getBytes(), hmac2.getBytes());
    }
}
