package com.psbags.PSBags.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psbags.PSBags.DTO.response.TrackingResponse;
import com.psbags.PSBags.Service.ShiprocketService;

@RestController
@RequestMapping("/public/shiprocket")
public class ShiprocketController {
    

    @Autowired
    private ShiprocketService shiprocketService;

    @GetMapping("/getTracking/{shipmentId}")
    public TrackingResponse getTrackingData(@PathVariable String shipmentId) throws Exception {
        return shiprocketService.getTrackingData(shipmentId);
    }
}
