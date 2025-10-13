package com.garja.Garja.DTO.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class TrackingResponseTest {

    @Test
public void testDeserializeTrackingResponse() throws IOException {
    // JSON content matching the structure from the error report
    String jsonContent = "{\n" +
            "    \"tracking_data\": {\n" +
            "        \"track_status\": 1,\n" +
            "        \"shipment_status\": 7,\n" +
            "        \"shipment_track\": [\n" +
            "            {\n" +
            "                \"id\": 966794345,\n" +
            "                \"awb_code\": \"SF1956944161KR\",\n" +
            "                \"courier_company_id\": 58,\n" +
            "                \"shipment_id\": 966794345,\n" +
            "                \"order_id\": 970375411,\n" +
            "                \"pickup_date\": \"2025-09-22 04:42:33\",\n" +
            "                \"delivered_date\": \"2025-09-26 16:45:42\",\n" +
            "                \"weight\": \"0.90\",\n" +
            "                \"packages\": 1,\n" +
            "                \"current_status\": \"Delivered\",\n" +
            "                \"delivered_to\": \"Nashik\",\n" +
            "                \"destination\": \"Nashik\",\n" +
            "                \"consignee_name\": \"Sandip\",\n" +
            "                \"origin\": \"Ahmed Nagar\",\n" +
            "                \"courier_agent_details\": {\n" +
            "                    \"delivery_boy_name\": \"Pankaj Ramesh Ahire .\",\n" +
            "                    \"delivery_boy_phone\": \"8600594755\",\n" +
            "                    \"ofd_time\": \"2025-09-26 11:50:06\",\n" +
            "                    \"tracking_status\": \"17\"\n" +
            "                },\n" +
            "                \"courier_name\": \"Shadowfax Surface\",\n" +
            "                \"edd\": \"2025-09-26 23:00:00\",\n" +
            "                \"pod\": \"Available\",\n" +
            "                \"pod_status\": \"OTP Based delivery\",\n" +
            "                \"rto_delivered_date\": \"NA\",\n" +
            "                \"return_awb_code\": \"\",\n" +
            "                \"updated_time_stamp\": \"2025-09-26 16:45:42\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"shipment_track_activities\": [\n" +
            "            {\n" +
            "                \"date\": \"2025-09-26 16:45:42\",\n" +
            "                \"status\": \"delivered\",\n" +
            "                \"activity\": \"Item Delivered at NSK_Satana\",\n" +
            "                \"location\": \"NSK_Satana\",\n" +
            "                \"sr-status\": \"7\",\n" +
            "                \"sr-status-label\": \"DELIVERED\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"track_url\": \"https://shiprocket.co/tracking/SF1956944161KR\",\n" +
            "        \"etd\": \"2025-09-26 23:00:00\",\n" +
            "        \"qc_response\": \"\",\n" +
            "        \"is_return\": false,\n" +
            "        \"order_tag\": \"\"\n" +
            "    }\n" +
            "}";

    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

    // This should not throw an exception now
    TrackingResponse response = mapper.readValue(jsonContent, TrackingResponse.class);
    
    // Verify that we can access the data
    assertNotNull(response);
    assertNotNull(response.getTrackingData());
    assertNotNull(response.getTrackingData().getShipmentTrackActivities());
    assertFalse(response.getTrackingData().getShipmentTrackActivities().isEmpty());
    
    // Verify courier agent details
    assertNotNull(response.getTrackingData().getShipment_track());
    assertFalse(response.getTrackingData().getShipment_track().isEmpty());
    assertNotNull(response.getTrackingData().getShipment_track().get(0).getCourier_agent_details());
    
    // Verify sr-status-label
    assertEquals("DELIVERED", response.getTrackingData().getShipmentTrackActivities().get(0).getSrStatusLabel());
}
}