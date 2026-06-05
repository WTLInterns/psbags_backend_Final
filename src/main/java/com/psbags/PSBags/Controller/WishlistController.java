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
        try {
            return ResponseEntity.ok(wishlistService.addToWishlist(getAuthenticatedEmail(), request.getProductId()));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required");
        } catch (RuntimeException e) {
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equalsIgnoreCase(authentication.getName())) {
            throw new SecurityException("Authentication required");
        }
        return authentication.getName();
    }
}
