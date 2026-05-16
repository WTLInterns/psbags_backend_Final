package com.psbags.PSBags.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psbags.PSBags.DTO.requests.LoginRequests;
import com.psbags.PSBags.DTO.requests.SignupRequests;
import com.psbags.PSBags.DTO.response.LoginResponse;
import com.psbags.PSBags.DTO.response.SignupResponse;
import com.psbags.PSBags.Exception.CustomException;
import com.psbags.PSBags.Service.AuthService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;



    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequests requests) {
        try {
            SignupResponse response = authService.signUp(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Something went wrong");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequests loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        
        if (response.getToken() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        return ResponseEntity.ok(response);
    }
    
}
