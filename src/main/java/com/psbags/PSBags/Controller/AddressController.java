package com.psbags.PSBags.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psbags.PSBags.DTO.requests.AddressRequest;
import com.psbags.PSBags.DTO.response.AddressResponse;
import com.psbags.PSBags.Model.Address;
import com.psbags.PSBags.Service.AddressService;

@RestController
@RequestMapping("/user/address")
public class AddressController {
    

    @Autowired
    private AddressService addressService;


    @PostMapping("/add")
    public AddressResponse addAddress(@RequestBody AddressRequest address){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return this.addressService.addAddress(email, address);
    }


    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable int id){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        this.addressService.deleteAddress(id,email);
    }


    @GetMapping("/byUser")
    public List<AddressResponse> getAllAddressByUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
            return this.addressService.getAllAddressByUser(email);
    }



}
