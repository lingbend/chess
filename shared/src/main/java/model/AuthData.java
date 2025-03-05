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

    public void SetAuthToken(String newToken) {
        authToken = newToken;
    }

    public String GetAuthToken() {
        return authToken;
    }

    public void SetUsername(String name) {
        username = name;
    }

    public String GetUsername() {
        return username;
    }

    public static String makeAuthToken() {
        Instant time = Instant.now();
        Random rand = new Random();
        int mod = rand.nextInt(10000);
        long longTime = time.getEpochSecond() / 2;
        long longRand = rand.nextLong() / 2;
        long longCat = (longTime + longRand) % mod;
        String auth = String.valueOf(longCat);
        return auth;
    }
}
