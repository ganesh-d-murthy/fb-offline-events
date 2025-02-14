package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class FacebookOfflineEventSender {
    private static final Logger logger = LoggerFactory.getLogger(FacebookOfflineEventSender.class);
    private static MessageDigest digest;

    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN"; // Replace with your actual token
    private static final String PIXEL_ID = "YOUR_PIXEL_ID"; // Replace with your actual pixel ID
    private static final String TEST_EVENT_CODE = "TEST12345"; // Replace with your actual test code

    private static final String FACEBOOK_API_URL = "https://graph.facebook.com/v16.0/" + PIXEL_ID + "/events";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public FacebookOfflineEventSender() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public static void sendEvent() {
        try {
            OkHttpClient client = new OkHttpClient();

            String jsonBody = "{"
                    + "\"data\": [{"
                    + "  \"event_name\": \"Purchase\","
                    + "  \"event_time\": " + (System.currentTimeMillis() / 1000) + ","
                    + "  \"action_source\": \"website\","
                    + "  \"test_event_code\": \"" + TEST_EVENT_CODE + "\","
                    + "  \"user_data\": {"
                    + "    \"em\": [\" + " + toSHA256String("test@example.com") + "\"],"
                    + "    \"client_ip_address\": \"127.0.0.1\","
                    + "    \"client_user_agent\": \"Mozilla/5.0\""
                    + "  },"
                    + "  \"custom_data\": {"
                    + "    \"value\": 100.00,"
                    + "    \"currency\": \"USD\""
                    + "  }"
                    + "}]}";

            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody);

            Request request = new Request.Builder()
                    .url("https://graph.facebook.com/v16.0/" + PIXEL_ID + "/events")
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Authorization", "Bearer " + ACCESS_TOKEN)
                    .build();

            Response response = client.newCall(request).execute();
            System.out.println("Response: " + response.body().string());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FacebookOfflineEventSender sender = new FacebookOfflineEventSender();
        sender.sendEvent();
    }

    public static String toSHA256String(String str) throws UnsupportedEncodingException {
        getSHA256MessageDigest();
        byte[] hash = digest.digest(str.getBytes("UTF-8"));
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }

        return result.toString();
    }

    /** Returns SHA-256 hashing algorithm. */
    private static void getSHA256MessageDigest() {
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Missing SHA-256 algorithm implementation.", e);
        }
    }
}