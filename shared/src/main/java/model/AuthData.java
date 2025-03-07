package model;

import java.lang.String;
import java.time.Instant;
import java.util.Random;
import java.io.Console;

public class AuthData {

    String authToken = null;
    String username = null;

    public AuthData(String name, String token){
        username = name;
        authToken = token;
    }

    public void setAuthToken(String newToken) {
        authToken = newToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setUsername(String name) {
        username = name;
    }

    public String getUsername() {
        return username;
    }

    public static String makeAuthToken() {
        Instant time = Instant.now();
        Random rand = new Random();
        int mod = rand.nextInt(10000);
        long longTime = Long.divideUnsigned(time.getEpochSecond(), 2);
        long longRand = Long.divideUnsigned(rand.nextLong(), 2);
        long longCat = (longTime + longRand) % mod;
        String auth = String.valueOf(longCat);
        return auth;
    }
}
