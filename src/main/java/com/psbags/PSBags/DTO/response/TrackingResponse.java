package com.psbags.PSBags.DTO.response;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class TrackingResponse {

    // Map to hold dynamic AWB/Shipment keys at root level (hidden from JSON output)
    @JsonIgnore
    private Map<String, TrackingDataWrapper> trackingDataMap = new HashMap<>();

    // Direct root-level tracking_data field as returned by the API
    @JsonProperty("tracking_data")
    private TrackingData trackingData;

    public Map<String, TrackingDataWrapper> getTrackingDataMap() {
        return trackingDataMap;
    }

    public void setTrackingDataMap(Map<String, TrackingDataWrapper> trackingDataMap) {
        this.trackingDataMap = trackingDataMap;
    }

    @JsonAnySetter
    public void set(String key, TrackingDataWrapper value) {
        trackingDataMap.put(key, value);
    }

    @JsonProperty("tracking_data")
    public TrackingData getTrackingData() {
        return trackingData;
    }

    @JsonProperty("tracking_data")
    public void setTrackingData(TrackingData trackingData) {
        this.trackingData = trackingData;
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TrackingDataWrapper {
    @JsonProperty("tracking_data")
    private TrackingData trackingData;

    public TrackingData getTrackingData() { return trackingData; }
    public void setTrackingData(TrackingData trackingData) { this.trackingData = trackingData; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class TrackingData {
    private int track_status;
    private int shipment_status;
    @JsonProperty("shipment_track")
    private List<ShipmentTrack> shipment_track;

    @JsonProperty("shipment_track_activities")
    private List<ShipmentTrackActivity> shipmentTrackActivities;

    private String track_url;
    private String etd; // sometimes EDD/ETD
    private String qc_response;
    private Boolean is_return;
    private String order_tag;
    private String error; // present when no activities yet

    // Getters and Setters
    public int getTrack_status() { return track_status; }
    public void setTrack_status(int track_status) { this.track_status = track_status; }
    public int getShipment_status() { return shipment_status; }
    public void setShipment_status(int shipment_status) { this.shipment_status = shipment_status; }
    public List<ShipmentTrack> getShipment_track() { return shipment_track; }
    public void setShipment_track(List<ShipmentTrack> shipment_track) { this.shipment_track = shipment_track; }
    public List<ShipmentTrackActivity> getShipmentTrackActivities() { return shipmentTrackActivities; }
    public void setShipmentTrackActivities(List<ShipmentTrackActivity> shipmentTrackActivities) { this.shipmentTrackActivities = shipmentTrackActivities; }
    public String getTrack_url() { return track_url; }
    public void setTrack_url(String track_url) { this.track_url = track_url; }
    public String getEtd() { return etd; }
    public void setEtd(String etd) { this.etd = etd; }
    public String getQc_response() { return qc_response; }
    public void setQc_response(String qc_response) { this.qc_response = qc_response; }
    public Boolean getIs_return() { return is_return; }
    public void setIs_return(Boolean is_return) { this.is_return = is_return; }
    public String getOrder_tag() { return order_tag; }
    public void setOrder_tag(String order_tag) { this.order_tag = order_tag; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ShipmentTrack {
    private Long id;
    private String awb_code;
    private Integer courier_company_id; // can be null
    private Long shipment_id;
    private Long order_id;
    private String pickup_date;
    private String delivered_date;
    private String weight;
    private Integer packages; // can be null
    private String current_status;
    private String delivered_to;
    private String destination;
    private String consignee_name;
    private String origin;
    private CourierAgentDetails courier_agent_details;
    private String courier_name;
    private String edd;
    private String pod;
    private String pod_status;
    private String rto_delivered_date;
    private String return_awb_code;
    private String updated_time_stamp;

    // Getters & Setters (optional if you plan to read values)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAwb_code() { return awb_code; }
    public void setAwb_code(String awb_code) { this.awb_code = awb_code; }
    public Integer getCourier_company_id() { return courier_company_id; }
    public void setCourier_company_id(Integer courier_company_id) { this.courier_company_id = courier_company_id; }
    public Long getShipment_id() { return shipment_id; }
    public void setShipment_id(Long shipment_id) { this.shipment_id = shipment_id; }
    public Long getOrder_id() { return order_id; }
    public void setOrder_id(Long order_id) { this.order_id = order_id; }
    public String getPickup_date() { return pickup_date; }
    public void setPickup_date(String pickup_date) { this.pickup_date = pickup_date; }
    public String getDelivered_date() { return delivered_date; }
    public void setDelivered_date(String delivered_date) { this.delivered_date = delivered_date; }
    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    public Integer getPackages() { return packages; }
    public void setPackages(Integer packages) { this.packages = packages; }
    public String getCurrent_status() { return current_status; }
    public void setCurrent_status(String current_status) { this.current_status = current_status; }
    public String getDelivered_to() { return delivered_to; }
    public void setDelivered_to(String delivered_to) { this.delivered_to = delivered_to; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getConsignee_name() { return consignee_name; }
    public void setConsignee_name(String consignee_name) { this.consignee_name = consignee_name; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public CourierAgentDetails getCourier_agent_details() { return courier_agent_details; }
    public void setCourier_agent_details(CourierAgentDetails courier_agent_details) { this.courier_agent_details = courier_agent_details; }
    public String getCourier_name() { return courier_name; }
    public void setCourier_name(String courier_name) { this.courier_name = courier_name; }
    public String getEdd() { return edd; }
    public void setEdd(String edd) { this.edd = edd; }
    public String getPod() { return pod; }
    public void setPod(String pod) { this.pod = pod; }
    public String getPod_status() { return pod_status; }
    public void setPod_status(String pod_status) { this.pod_status = pod_status; }
    public String getRto_delivered_date() { return rto_delivered_date; }
    public void setRto_delivered_date(String rto_delivered_date) { this.rto_delivered_date = rto_delivered_date; }
    public String getReturn_awb_code() { return return_awb_code; }
    public void setReturn_awb_code(String return_awb_code) { this.return_awb_code = return_awb_code; }
    public String getUpdated_time_stamp() { return updated_time_stamp; }
    public void setUpdated_time_stamp(String updated_time_stamp) { this.updated_time_stamp = updated_time_stamp; }
}

// New class to represent courier agent details
@JsonIgnoreProperties(ignoreUnknown = true)
class CourierAgentDetails {
    private String delivery_boy_name;
    private String delivery_boy_phone;
    private String ofd_time;
    private String tracking_status;

    // Getters and setters
    public String getDelivery_boy_name() { return delivery_boy_name; }
    public void setDelivery_boy_name(String delivery_boy_name) { this.delivery_boy_name = delivery_boy_name; }
    public String getDelivery_boy_phone() { return delivery_boy_phone; }
    public void setDelivery_boy_phone(String delivery_boy_phone) { this.delivery_boy_phone = delivery_boy_phone; }
    public String getOfd_time() { return ofd_time; }
    public void setOfd_time(String ofd_time) { this.ofd_time = ofd_time; }
    public String getTracking_status() { return tracking_status; }
    public void setTracking_status(String tracking_status) { this.tracking_status = tracking_status; }
}

@JsonIgnoreProperties(ignoreUnknown = true)
class ShipmentTrackActivity {
    private String date;
    private String status;
    private String activity;
    private String location;

    @JsonProperty("sr-status")
    private String srStatus;
    
    @JsonProperty("sr-status-label")
    private String srStatusLabel;

    // Getters & Setters (optional if you plan to read values)
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSrStatus() { return srStatus; }
    public void setSrStatus(String srStatus) { this.srStatus = srStatus; }
    public String getSrStatusLabel() { return srStatusLabel; }
    public void setSrStatusLabel(String srStatusLabel) { this.srStatusLabel = srStatusLabel; }
}
