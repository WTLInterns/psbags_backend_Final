package com.garja.Garja.Service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.garja.Garja.DTO.response.TrackingResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ShiprocketService {

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build();

    private final MediaType JSON = MediaType.parse("application/json");

    private String login() throws Exception {
        String loginPayload = "{\n" +
                "  \"email\": \"rse9733@gmail.com\",\n" +
                "  \"password\": \"gj$fA1I1!SdDdQO$\"\n" +
                "}";
        RequestBody loginBody = RequestBody.create(JSON, loginPayload);

        Request loginRequest = new Request.Builder()
                .url("https://apiv2.shiprocket.in/v1/external/auth/login")
                .post(loginBody)
                .addHeader("Content-Type", "application/json")
                .build();

        Response loginResponse = client.newCall(loginRequest).execute();
        String loginResponseBody = loginResponse.body().string();

        JSONObject loginJson = new JSONObject(loginResponseBody);
        return loginJson.getString("token");
    }

    public String createOrder(
        String firstName, String lastName, String email, String phoneNumber,
        String address, String city, String state, String country, String pincode,
        List<String> productNames, String paymentType, List<Integer> quantities, List<Double> prices
) throws Exception {
    String token = login();

    long millis = System.currentTimeMillis();
    String uniquePart = String.valueOf(millis).substring(4, 13);
    String orderId = "GG-ORDER-" + uniquePart;
    String orderDate = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    System.out.println(token);

    String phoneDigits = (phoneNumber == null ? "" : phoneNumber.replaceAll("[^0-9]", ""));
    if (phoneDigits.length() > 10) {
        phoneDigits = phoneDigits.substring(phoneDigits.length() - 10);
    }
    if (phoneDigits.length() != 10) {
        throw new IllegalArgumentException("Invalid billing phone. Provide a 10-digit number. Provided: " + phoneNumber);
    }
    long billingPhone = Long.parseLong(phoneDigits);

    String srPaymentMethod = (paymentType != null && paymentType.equalsIgnoreCase("COD")) ? "COD" : "Prepaid";

    StringBuilder orderItemsBuilder = new StringBuilder();
    double totalAmount = 0;
    for (int i = 0; i < productNames.size(); i++) {
        String sku = "SKU-" + (millis + i); 
        int qty = quantities.get(i);
        double price = prices.get(i);
        totalAmount += (price * qty);

        orderItemsBuilder.append("    {\n")
                .append("      \"name\": \"").append(productNames.get(i)).append("\",\n")
                .append("      \"sku\": \"").append(sku).append("\",\n")
                .append("      \"units\": ").append(qty).append(",\n")
                .append("      \"selling_price\": ").append(price).append(",\n")
                .append("      \"tax_amount\": 0,\n")
                .append("      \"discount\": 0\n")
                .append("    }");

        if (i != productNames.size() - 1) {
            orderItemsBuilder.append(",\n");
        } else {
            orderItemsBuilder.append("\n");
        }
    }

    String orderPayload = "{\n" +
            "  \"order_id\": \"" + orderId + "\",\n" +
            "  \"order_date\": \"" + orderDate + "\",\n" +
            "  \"pickup_location\": \"Home\",\n" +
            "  \"channel_id\": 8372908,\n" +
            "  \"billing_customer_name\": \"" + firstName + "\",\n" +
            "  \"billing_last_name\": \"" + lastName + "\",\n" +
            "  \"billing_email\": \"" + email + "\",\n" +
            "  \"billing_phone\": \"91" + billingPhone + "\",\n" +
            "  \"billing_address\": \"" + address + "\",\n" +
            "  \"billing_city\": \"" + city + "\",\n" +
            "  \"billing_state\": \"" + "Maharashtra" + "\",\n" +
            "  \"billing_country\": \"India\",\n" +
            "  \"billing_pincode\": \"" + pincode + "\",\n" +
            "  \"shipping_is_billing\": true,\n" +
            "  \"payment_method\": \"" + srPaymentMethod + "\",\n" +
            "  \"order_items\": [\n" + orderItemsBuilder + "  ],\n" +
            "  \"sub_total\": " + totalAmount + ",\n" +
            "  \"total\": " + totalAmount + ",\n" +
            "  \"length\": 30,\n" +
            "  \"breadth\": 20,\n" +
            "  \"height\": 10,\n" +
            "  \"weight\": 1.2\n" +
            "}";

    RequestBody orderBody = RequestBody.create(JSON, orderPayload);

    Request orderRequest = new Request.Builder()
            .url("https://apiv2.shiprocket.in/v1/external/orders/create/adhoc")
            .post(orderBody)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + token)
            .build();

    Response orderResponse = client.newCall(orderRequest).execute();
    return orderResponse.body().string();
}

    public TrackingResponse getTrackingData(String shipmentId) throws Exception {
        String token = login();

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://apiv2.shiprocket.in/v1/external/courier/track/awb/" + shipmentId)
                .get() 
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + token)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            String errBody = response.body() != null ? response.body().string() : "";
            throw new RuntimeException("Shiprocket track API failed: " + response.code() + " - " + errBody);
        }

        String jsonResponse = response.body() != null ? response.body().string() : "{}";

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
        return mapper.readValue(jsonResponse, TrackingResponse.class);
    }
    
}
