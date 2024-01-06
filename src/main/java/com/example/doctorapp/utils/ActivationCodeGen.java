package com.example.doctorapp.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

//used to creating activation code for the user account
public class ActivationCodeGen {

    //Generating activation code relying on user_id and salt

    public static String generateActivationCode(String userId, String salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            String combinedData = userId + salt; // Combining user_id and the given salt
            byte[] hashBytes = digest.digest(combinedData.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hashBytes); //Converting to hexadecimal representation
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    public static boolean verifyActivationCode(String userId, String activationCode ,String salt) {
        //Generating activation code with the same data set
        String generatedCode = generateActivationCode(userId, salt);

        //Comparing both codes
        return generatedCode != null && generatedCode.equals(activationCode);
    }
}
