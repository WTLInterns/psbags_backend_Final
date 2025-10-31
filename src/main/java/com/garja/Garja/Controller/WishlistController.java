package com.garja.Garja.Controller;

import com.garja.Garja.DTO.response.WishlistResponse;
import com.garja.Garja.Service.WishlistService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user/wishlist")
public class WishlistController {

    private final WishlistService wishlistService;

    //add product to wishlist
    @PostMapping("/{productId}")
    public ResponseEntity<String> addToWishlist(@PathVariable int productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.addToWishlist(email,productId));
    }


    @DeleteMapping("/{productId}")
    public ResponseEntity<String> removeProductFromWishlist(@PathVariable int productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.removeProductFromWishlist(email,productId));
    }

    // remove the wishlist
    @DeleteMapping("/{wishlistId}")
    public ResponseEntity<String> removeFromWishlist(@PathVariable int wishlistId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.removeFromWishlist(wishlistId, email));
    }

    // get user wishlist
    @GetMapping
    public ResponseEntity<List<WishlistResponse>> getUserWishlist(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ResponseEntity.ok(wishlistService.getUserWishlist(email));
    }
}