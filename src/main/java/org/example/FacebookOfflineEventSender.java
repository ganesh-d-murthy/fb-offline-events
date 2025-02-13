package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FacebookOfflineEventSender {
    private static final Logger logger = LoggerFactory.getLogger(FacebookOfflineEventSender.class);

    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN"; // Replace with your actual token
    private static final String PIXEL_ID = "YOUR_PIXEL_ID"; // Replace with your actual pixel ID
    private static final String TEST_EVENT_CODE = "TEST12345"; // Replace with your actual test code

    private static final String FACEBOOK_API_URL = "https://graph.facebook.com/v18.0/" + PIXEL_ID + "/events";

    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public FacebookOfflineEventSender() {
        this.client = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public void sendEvent() {
        try {
            // Build request payload
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("event_name", "Purchase");
            eventData.put("event_time", System.currentTimeMillis() / 1000);
            eventData.put("action_source", "offline");

            Map<String, Object> customData = new HashMap<>();
            customData.put("value", 100.00);
            customData.put("currency", "USD");

            eventData.put("custom_data", customData);
            eventData.put("test_event_code", TEST_EVENT_CODE); // Ensures this is a test event

            Map<String, Object> payload = new HashMap<>();
            payload.put("data", new Object[]{eventData});
            payload.put("access_token", ACCESS_TOKEN);

            // Convert to JSON
            String jsonPayload = objectMapper.writeValueAsString(payload);

            // Create HTTP request
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonPayload);
            Request request = new Request.Builder()
                    .url(FACEBOOK_API_URL)
                    .post(body)
                    .build();

            // Send request
            logger.info("Sending Facebook Offline Event...");
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                logger.info("Success: " + response.body().string());
            } else {
                logger.error("Failed: " + response.code() + " - " + response.body().string());
            }

        } catch (IOException e) {
            logger.error("Error sending event", e);
        }
    }

    public static void main(String[] args) {
        FacebookOfflineEventSender sender = new FacebookOfflineEventSender();
        sender.sendEvent();
    }
}