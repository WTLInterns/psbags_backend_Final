package com.psbags.PSBags.Controller;

import com.psbags.PSBags.DTO.requests.BuyNowRequest;
import com.psbags.PSBags.DTO.response.OrderResponse;
import com.psbags.PSBags.Service.OrderService;
import com.psbags.PSBags.Repo.UserRepo;
import com.psbags.PSBags.Model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import com.psbags.PSBags.DTO.requests.RazorpayOrderRequest;
import com.psbags.PSBags.DTO.requests.RazorpayPaymentVerificationRequest;
import org.json.JSONObject;
import com.razorpay.RazorpayException;




@RestController
@RequestMapping("/user/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserRepo userRepository;


//use mai nahi hai abhi hai api 
    @PostMapping("/buy-now")
    public ResponseEntity<OrderResponse> buyNow(@RequestBody BuyNowRequest request) {
        try {
            // Get user ID from JWT token instead of path parameter for security
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);
            
            OrderResponse response = orderService.buyNow(userId, request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            OrderResponse errorResponse = new OrderResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @PostMapping("/checkout")
    public ResponseEntity<OrderResponse> checkoutCart(@RequestParam int addressId) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);
            
            OrderResponse response = orderService.checkoutCart(userId, addressId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            OrderResponse errorResponse = new OrderResponse();
            errorResponse.setMessage(e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
            
        }
 
    }

    

    // create razorpay order create to initiate payment
    @PostMapping("/create-razorpay-order")
    public ResponseEntity<?> createRazorpayOrder(@RequestBody RazorpayOrderRequest request) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);

            var order = orderService.createRazorpayOrder(request, userId);
            return ResponseEntity.ok(order);
        } catch (RazorpayException ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create Razorpay order: " + ex.getMessage());
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    // verify payment and save order details
    @PostMapping("/verify-payment")
    public ResponseEntity<?> verifyPayment(@RequestBody RazorpayPaymentVerificationRequest request, @RequestParam int addressId) throws Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);

            OrderResponse response = orderService.verifyAndSaveOrder(request, userId, addressId);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<OrderResponse>> getPurchaseHistory() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();
            Integer userId = getUserIdFromEmail(email);

            List<OrderResponse> history = orderService.getPurchaseHistory(userId);
            return ResponseEntity.ok(history);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // helper method to get user
    private Integer getUserIdFromEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("Email is required");
            }
            
            User user = userRepository.findByEmail(email).orElseThrow();
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            return user.getId();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get user: " + e.getMessage());
        }
    }
}
