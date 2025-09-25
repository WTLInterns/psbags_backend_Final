package com.garja.Garja.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.garja.Garja.Service.ShiprocketService;

@RestController
@RequestMapping("/public")
public class HelloController {


    @Autowired
    private  ShiprocketService shiprocketService;

   

    // @GetMapping("/hello")
    // public String createOrder() {
    //     try {
    //         // Call service to login and create order
    //         return shiprocketService.createOrder();
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return "Error creating order: " + e.getMessage();
    //     }
    // }
}

