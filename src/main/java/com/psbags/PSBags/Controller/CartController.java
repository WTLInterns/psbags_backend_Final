package com.psbags.PSBags.Controller;

import com.psbags.PSBags.DTO.requests.CartAddRequest;
import com.psbags.PSBags.DTO.response.CartResponse;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.UserRepo;
import com.psbags.PSBags.Service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping({"/user/cart", "/api/cart"})
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserRepo userRepository;

    @PostMapping("/add/{productId}")
    public ResponseEntity<CartResponse> addProductToCart(
            @PathVariable Integer productId,
            @RequestParam(defaultValue = "1") int quantity) {
        
        System.out.println("==================== DEBUG START - CART CONTROLLER ====================");
        System.out.println("[CART REQUEST]");
        System.out.println("[CART] Endpoint: POST /user/cart/add/{productId}");
        System.out.println("[CART] Product ID: " + productId);
        System.out.println("[CART] Quantity: " + quantity);
        
        try {
            Integer userId = getUserIdFromJWT();
            System.out.println("[CART] Authenticated User ID: " + userId);
            System.out.println("[CART] ✅ Authentication successful - adding to cart");
            
            CartResponse cartResponse = cartService.addProductToCart(userId, productId, quantity);
            
            System.out.println("[CART] ✅ Cart updated successfully");
            System.out.println("[CART] Response Status: 200 OK");
            System.out.println("==================== DEBUG END - CART CONTROLLER ====================");
            
            return ResponseEntity.ok(cartResponse);
        } catch (SecurityException e) {
            System.err.println("[CART] ❌ Security exception: " + e.getMessage());
            System.err.println("[CART] Response Status: 401 UNAUTHORIZED");
            System.out.println("==================== DEBUG END - CART CONTROLLER ====================");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            System.err.println("[CART] ❌ Runtime exception: " + e.getMessage());
            System.err.println("[CART] Response Status: 400 BAD REQUEST");
            System.out.println("==================== DEBUG END - CART CONTROLLER ====================");
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProductToCart(@Valid @RequestBody CartAddRequest request) {
        System.out.println("==================== DEBUG START - CART CONTROLLER (JSON) ====================");
        System.out.println("[CART REQUEST]");
        System.out.println("[CART] Endpoint: POST /api/cart/add");
        System.out.println("[CART] Product ID: " + request.getProductId());
        System.out.println("[CART] Quantity: " + request.getQuantity());
        
        try {
            Integer userId = getUserIdFromJWT();
            System.out.println("[CART] Authenticated User ID: " + userId);
            System.out.println("[CART] ✅ Authentication successful - adding to cart");
            
            int quantity = request.getQuantity() == null ? 1 : request.getQuantity();
            CartResponse cartResponse = cartService.addProductToCart(userId, request.getProductId(), quantity);
            
            System.out.println("[CART] ✅ Cart updated successfully");
            System.out.println("[CART] Response Status: 200 OK");
            System.out.println("==================== DEBUG END - CART CONTROLLER (JSON) ====================");
            
            return ResponseEntity.ok(cartResponse);
        } catch (SecurityException e) {
            System.err.println("[CART] ❌ Security exception: " + e.getMessage());
            System.err.println("[CART] Response Status: 401 UNAUTHORIZED");
            System.out.println("==================== DEBUG END - CART CONTROLLER (JSON) ====================");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            System.err.println("[CART] ❌ Runtime exception: " + e.getMessage());
            System.err.println("[CART] Response Status: 400 BAD REQUEST");
            System.out.println("==================== DEBUG END - CART CONTROLLER (JSON) ====================");
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart() {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.getCart(userId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<CartResponse> removeProductFromCart(@PathVariable Integer productId) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.removeProductFromCart(userId, productId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<CartResponse> updateProductQuantity(
            @PathVariable Integer productId,
            @RequestParam int quantity) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.updateProductQuantity(userId, productId, quantity));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/size/{productId}")
    public ResponseEntity<CartResponse> updateProductSize(
            @PathVariable Integer productId,
            @RequestParam String size) {
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.updateProductSize(userId, productId, size));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart() {
        try {
            Integer userId = getUserIdFromJWT();
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Failed to clear cart");
        }
    }

    // Helper method to get userId from JWT token
    private Integer getUserIdFromJWT() {
        System.out.println("==================== DEBUG START - GET USER FROM JWT ====================");
        System.out.println("[CART] Extracting user ID from JWT...");
        
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            
            System.out.println("[CART] Security Context Authentication: " + authentication);
            System.out.println("[CART] Authentication is null: " + (authentication == null));
            
            if (authentication != null) {
                System.out.println("[CART] Authentication class: " + authentication.getClass().getName());
                System.out.println("[CART] Authentication name: " + authentication.getName());
                System.out.println("[CART] Is authenticated: " + authentication.isAuthenticated());
                System.out.println("[CART] Authorities: " + authentication.getAuthorities());
            }
            
            if(authentication == null
                    || !authentication.isAuthenticated()
                    || authentication.getName() == null
                    || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
                System.err.println("[CART] ❌ Authentication check failed");
                System.err.println("[CART] Throwing SecurityException");
                System.out.println("==================== DEBUG END - GET USER FROM JWT ====================");
                throw new SecurityException("Authentication required");
            }
            
            String email = authentication.getName();
            System.out.println("[CART] Authenticated email: " + email);
            System.out.println("[CART] Looking up user in database...");
            
            User user = userRepository.findByEmail(email).orElseThrow();
            
            if (user == null) {
                System.err.println("[CART] ❌ User not found in database for email: " + email);
                System.out.println("==================== DEBUG END - GET USER FROM JWT ====================");
                throw new RuntimeException("User not found");
            }
            
            System.out.println("[CART] ✅ User found - ID: " + user.getId() + ", Email: " + user.getEmail());
            System.out.println("==================== DEBUG END - GET USER FROM JWT ====================");
            
            return user.getId();
        } catch (SecurityException e) {
            System.err.println("[CART] ❌ Security exception in getUserIdFromJWT: " + e.getMessage());
            System.out.println("==================== DEBUG END - GET USER FROM JWT ====================");
            throw e;
        } catch (Exception e) {
            System.err.println("[CART] ❌ Exception in getUserIdFromJWT: " + e.getMessage());
            e.printStackTrace();
            System.out.println("==================== DEBUG END - GET USER FROM JWT ====================");
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
