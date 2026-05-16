package com.psbags.PSBags.DTO.requests;

import lombok.Data;
import java.util.List;

@Data
public class ShiprocketWebhookPayload {
    private String awb;
    private String current_status;
    private String order_id;
    private String current_timestamp;
    private String etd;
    private int current_status_id;
    private String shipment_status;
    private int shipment_status_id;
    private String channel_order_id;
    private String channel;
    private String courier_name;
    private List<ScanEvent> scans;

    @Data
    public static class ScanEvent {
        private String date;
        private String activity;
        private String location;
    }
}
