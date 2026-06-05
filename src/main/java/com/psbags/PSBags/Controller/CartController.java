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
        try {
            Integer userId = getUserIdFromJWT();
            return ResponseEntity.ok(cartService.addProductToCart(userId, productId, quantity));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<CartResponse> addProductToCart(@Valid @RequestBody CartAddRequest request) {
        try {
            Integer userId = getUserIdFromJWT();
            int quantity = request.getQuantity() == null ? 1 : request.getQuantity();
            return ResponseEntity.ok(cartService.addProductToCart(userId, request.getProductId(), quantity));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
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
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null
                    || !authentication.isAuthenticated()
                    || authentication.getName() == null
                    || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
                throw new SecurityException("Authentication required");
            }
            
            String email = authentication.getName();
            User user = userRepository.findByEmail(email).orElseThrow();
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            return user.getId();
        } catch (SecurityException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage());
        }
    }
}
