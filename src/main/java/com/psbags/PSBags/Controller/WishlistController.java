package com.psbags.PSBags.Controller;

import com.psbags.PSBags.DTO.requests.WishlistRequest;
import com.psbags.PSBags.DTO.response.WishlistResponse;
import com.psbags.PSBags.Service.WishlistService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping({"/user/wishlist", "/api/wishlist"})
public class WishlistController {

    private final WishlistService wishlistService;

    // add product to wishlist
    @PostMapping("/{productId}")
    public ResponseEntity<String> addToWishlist(@PathVariable int productId) {
        try {
            return ResponseEntity.ok(wishlistService.addToWishlist(getAuthenticatedEmail(), productId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToWishlist(@Valid @RequestBody WishlistRequest request) {
        System.out.println("==================== DEBUG START - WISHLIST CONTROLLER ====================");
        System.out.println("[WISHLIST REQUEST]");
        System.out.println("[WISHLIST] Endpoint: POST /api/wishlist/add");
        System.out.println("[WISHLIST] Product ID: " + request.getProductId());
        
        try {
            String email = getAuthenticatedEmail();
            System.out.println("[WISHLIST] Authenticated User Email: " + email);
            System.out.println("[WISHLIST] ✅ Authentication successful - adding to wishlist");
            
            String result = wishlistService.addToWishlist(email, request.getProductId());
            
            System.out.println("[WISHLIST] ✅ Wishlist updated successfully");
            System.out.println("[WISHLIST] Response: " + result);
            System.out.println("[WISHLIST] Response Status: 200 OK");
            System.out.println("==================== DEBUG END - WISHLIST CONTROLLER ====================");
            
            return ResponseEntity.ok(result);
        } catch (SecurityException e) {
            System.err.println("[WISHLIST] ❌ Security exception: " + e.getMessage());
            System.err.println("[WISHLIST] Response Status: 401 UNAUTHORIZED");
            System.out.println("==================== DEBUG END - WISHLIST CONTROLLER ====================");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
            System.err.println("[WISHLIST] ❌ Runtime exception: " + e.getMessage());
            System.err.println("[WISHLIST] Response Status: 400 BAD REQUEST");
            System.out.println("==================== DEBUG END - WISHLIST CONTROLLER ====================");
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // remove product from wishlist by product ID
    @DeleteMapping("/product/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(@PathVariable int productId) {
        try {
            return ResponseEntity.ok(wishlistService.removeProductFromWishlist(getAuthenticatedEmail(), productId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<String> removeProductFromWishlistAlias(@PathVariable int productId) {
        try {
            return ResponseEntity.ok(wishlistService.removeProductFromWishlist(getAuthenticatedEmail(), productId));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // remove the wishlist item by wishlist ID
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable int wishlistId) {
        try {
            return ResponseEntity.ok(wishlistService.removeFromWishlist(wishlistId, getAuthenticatedEmail()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // get user wishlist
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getUserWishlist() {
        try {
            return ResponseEntity.ok(wishlistService.getUserWishlist(getAuthenticatedEmail()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private String getAuthenticatedEmail() {
        System.out.println("==================== DEBUG START - GET AUTHENTICATED EMAIL ====================");
        System.out.println("[WISHLIST] Extracting authenticated email...");
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        System.out.println("[WISHLIST] Security Context Authentication: " + authentication);
        System.out.println("[WISHLIST] Authentication is null: " + (authentication == null));
        
        if (authentication != null) {
            System.out.println("[WISHLIST] Authentication class: " + authentication.getClass().getName());
            System.out.println("[WISHLIST] Authentication name: " + authentication.getName());
            System.out.println("[WISHLIST] Is authenticated: " + authentication.isAuthenticated());
            System.out.println("[WISHLIST] Authorities: " + authentication.getAuthorities());
        }
        
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
            System.err.println("[WISHLIST] ❌ Authentication check failed");
            System.err.println("[WISHLIST] Throwing SecurityException");
            System.out.println("==================== DEBUG END - GET AUTHENTICATED EMAIL ====================");
            throw new SecurityException("Authentication required");
        }
        
        String email = authentication.getName();
        System.out.println("[WISHLIST] ✅ Authenticated email: " + email);
        System.out.println("==================== DEBUG END - GET AUTHENTICATED EMAIL ====================");
        
        return email;
    }
}
