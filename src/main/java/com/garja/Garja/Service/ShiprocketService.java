package com.garja.Garja.Service;

import okhttp3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                "  \"email\": \"panditprashant5365@gmail.com\",\n" +
                "  \"password\": \"zwTy0B$ADt&fDi^G\"\n" +
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

    public String createOrder(String firstName,String lastName,String email,String phoneNumber,String address,String city,String state,String country,String pincode,String productName,String paymentType,int quantity,double totalAmount) throws Exception {
        String token = login();

        long millis = System.currentTimeMillis();
        String uniquePart = String.valueOf(millis).substring(4, 13);
        String sku = String.valueOf(millis).substring(4, 9);
        String orderId = "GG-ORDER-" + uniquePart;
        String orderDate = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                System.out.println(token);

        // Sanitize phone to digits and validate to exactly 10 digits (India)
        String phoneDigits = (phoneNumber == null ? "" : phoneNumber.replaceAll("[^0-9]", ""));
        if (phoneDigits.length() > 10) {
            // keep last 10 for local part when excessive digits (strip country code)
            phoneDigits = phoneDigits.substring(phoneDigits.length() - 10);
        }
        if (phoneDigits.length() != 10) {
            throw new IllegalArgumentException("Invalid billing phone. Provide a 10-digit number. Provided: " + phoneNumber);
        }
        long billingPhone = Long.parseLong(phoneDigits);

        // Map payment type to Shiprocket-accepted values
        String srPaymentMethod = (paymentType != null && paymentType.equalsIgnoreCase("COD")) ? "COD" : "Prepaid";

        String orderPayload = "{\n" +
                "  \"order_id\": \"" + orderId + "\",\n" +
                "  \"order_date\": \"" + orderDate + "\",\n" +
                "  \"pickup_location\": \"GRADUATE GULACHA CHAHA&LASSI PVTLTD\",\n" +
                "  \"channel_id\": 8207072,\n" +
                "  \"billing_customer_name\": \"" + firstName + "\",\n" +
                "  \"billing_last_name\": \"" + lastName + "\",\n" +
                "  \"billing_email\": \"" + email + "\",\n" +
                "  \"billing_phone\": " + billingPhone + ",\n" +
                "  \"billing_address\": \"" + address + "\",\n" +
                "  \"billing_city\": \"" + city + "\",\n" +
                "  \"billing_state\": \"" + state + "\",\n" +
                "  \"billing_country\": \"" + country + "\",\n" +
                "  \"billing_pincode\": \"" + pincode + "\",\n" +
                "  \"shipping_is_billing\": true,\n" +
                "  \"payment_method\": \"" + srPaymentMethod + "\",\n" +
                "  \"order_items\": [\n" +
                "    {\n" +
                "      \"name\": \"" + productName + "\",\n" +
                "      \"sku\": \"" + sku + "\",\n" +
                "      \"units\": " + quantity + ",\n" +
                "      \"selling_price\": " + totalAmount + ",\n" +
                "      \"tax_amount\": 0,\n" +
                "      \"discount\": 0\n" +
                "    }\n" +
                "  ],\n" +
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

    public void createOrder(String firstName, String lastName, String email, Long phoneNumber, String address, String city,
                String state, String country, String pincode, String productName, String paymentType, int quantity,
                double totalAmount) {
        try {
            // Delegate to the main method by converting phone number to String
            createOrder(firstName, lastName, email,
                    phoneNumber == null ? null : String.valueOf(phoneNumber),
                    address, city, state, country, pincode, productName, paymentType, quantity, totalAmount);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create Shiprocket order: " + e.getMessage(), e);
        }
    }
}
