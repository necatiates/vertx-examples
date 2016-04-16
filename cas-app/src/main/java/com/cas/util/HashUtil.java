package com.cas.util;

import io.vertx.ext.auth.jdbc.impl.JDBCAuthImpl;

import java.security.SecureRandom;
import java.util.Random;

/**
 * Created by tolga on 17.02.2016.
 */
public class HashUtil {
    public static String genSalt() {
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        return JDBCAuthImpl.bytesToHex(salt);
    }
    public static String hashWithSalt(String password , String salt){
       return JDBCAuthImpl.computeHash(password, salt, "SHA-512");
    }
}
