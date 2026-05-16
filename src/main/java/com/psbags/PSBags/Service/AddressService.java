package com.psbags.PSBags.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psbags.PSBags.DTO.requests.AddressRequest;
import com.psbags.PSBags.DTO.response.AddressResponse;
import com.psbags.PSBags.Model.Address;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.AddressRepo;
import com.psbags.PSBags.Repo.UserRepo;

@Service
public class AddressService {
    
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;


    public AddressResponse addAddress(String email, AddressRequest request){
        User user = this.userRepo.findByEmail(email).orElseThrow();
        Address address = new Address();
        address.setSteet(request.getSteet());
        address.setCity(request.getCity());
        address.setLandmark(request.getLandmark());
        address.setPincode(request.getPincode());
        address.setAddress(request.getAddress());
        address.setUser(user);
        addressRepo.save(address);
        return new AddressResponse(address.getId(),address.getSteet(),address.getCity(),address.getLandmark(),address.getPincode(),address.getAddress());

       
    }

    public void deleteAddress(int id, String email){
        User user = this.userRepo.findByEmail(email).orElseThrow();
        this.addressRepo.deleteById(id);
    }

    public List<AddressResponse> getAllAddressByUser(String email) {
        User user = this.userRepo.findByEmail(email).orElseThrow();
    if (user == null) {
        throw new RuntimeException("User not found with email: " + email);
    }

    return addressRepo.findAll().stream()
            .filter(a -> a.getUser().getId()==user.getId())
            .map(a -> new AddressResponse(
                   a.getId(),a.getSteet(),a.getCity(),a.getLandmark(),a.getPincode(),a.getAddress()
                
            ))
            .toList();
}







    
}
