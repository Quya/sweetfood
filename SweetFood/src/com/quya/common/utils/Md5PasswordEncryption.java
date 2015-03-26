package com.quya.common.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.log4j.Logger;

public class Md5PasswordEncryption implements PasswordEncryption {
    private static final int KEY1 = 0x000000ff;
    private static final int KEY2 = 0xffffff00;
    private static final int LENGTH = 6;

    public String encrypt(String password) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            byte[] sb = md5.digest();

            for (int i = 0; i < sb.length; i++) {
                result += Integer.toHexString((KEY1 & sb[i]) |
                        KEY2).substring(LENGTH);
            }

        } catch (NoSuchAlgorithmException ne) {
            Logger logger = Logger.getLogger(Md5PasswordEncryption.class);
            logger.error("no MD5 algorithm");

            result = "";
        }

        return result;

    }
}
