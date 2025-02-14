package org.example;

import com.facebook.ads.sdk.APIContext;
import com.facebook.ads.sdk.APIException;
import com.facebook.ads.sdk.serverside.ActionSource;
import com.facebook.ads.sdk.serverside.CustomData;
import com.facebook.ads.sdk.serverside.Event;
import com.facebook.ads.sdk.serverside.EventRequest;
import com.facebook.ads.sdk.serverside.EventResponse;
import com.facebook.ads.sdk.serverside.UserData;
import com.facebook.ads.sdk.serverside.EventRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class FacebookSDKEventSender {
    private static final Logger logger = LoggerFactory.getLogger(FacebookSDKEventSender.class);

    private static final String ACCESS_TOKEN = "YOUR_ACCESS_TOKEN"; // Replace with actual token
    private static final String PIXEL_ID = "YOUR_PIXEL_ID"; // Replace with actual pixel ID
    private static final String TEST_EVENT_CODE = "TEST12345"; // Replace with actual test code

    public void sendEvent() {
        try {
            logger.info("Initializing Facebook SDK...");

            APIContext context = new APIContext(ACCESS_TOKEN).enableDebug(true);

            // Create an EventRequest object
            EventRequest eventRequest = new EventRequest(PIXEL_ID, context);
            UserData userData = new UserData();
            userData.setEmail("test@example.com");
            userData.setPhone("123-456-7890");
            CustomData customData = new CustomData();
            customData.setValue(100.00f);
            customData.setCurrency("USD");
            Event event = new Event();
            event.eventName("test-Purchase")
                    .eventId("123456789")
                    .eventTime(System.currentTimeMillis() / 1000)
                    .actionSource(ActionSource.physical_store)
                    .userData(userData)
                    .customData(customData);
            eventRequest.addDataItem(event);

            String testEventCode = "";
                eventRequest.setTestEventCode(testEventCode);

            List<Event> events = Arrays.asList(event);
            eventRequest.setData(events);

            logger.info("Sending Facebook Offline Event...");

            // Execute the request
            EventResponse response = eventRequest.execute();

            logger.info("Success: Events received = " + response.getEventsReceived());

        } catch (APIException e) {
            logger.error("Facebook API error: " + e.getMessage(), e);
        }
    }

    public static void main(String[] args) {
        FacebookSDKEventSender sender = new FacebookSDKEventSender();
        sender.sendEvent();
    }
}
