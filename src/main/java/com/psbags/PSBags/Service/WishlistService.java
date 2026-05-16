package com.psbags.PSBags.Service;

import com.cloudinary.api.ApiResponse;
import com.psbags.PSBags.DTO.response.WishlistResponse;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Model.Wishlist;
import com.psbags.PSBags.Repo.ProductRepo;
import com.psbags.PSBags.Repo.UserRepo;
import com.psbags.PSBags.Repo.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepo userRepository;
    private final ProductRepo productRepository;

    public String addToWishlist(String email, int productId){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new RuntimeException("Product not found"));

        if (wishlistRepository.findByUserIdAndProductId(user.getId(), productId).isPresent()){
            return "Product Already in wishlist";
        }
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();
        wishlistRepository.save(wishlist);
        return "Product added to Wishlist";
    }

    public String removeFromWishlist(int wishlistId, String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
        
        Wishlist wishlist = wishlistRepository.findById(wishlistId)
                .orElseThrow(()->new RuntimeException("Wishlist item not found"));
        
        // Check if the wishlist item belongs to the user
        if (wishlist.getUser().getId() != user.getId()) {
            throw new RuntimeException("You are not authorized to delete this wishlist item");
        }
        
        wishlistRepository.delete(wishlist);
        return "Product removed from wishlist";
    }

    public List<WishlistResponse> getUserWishlist(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
        
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(user.getId());

        List<WishlistResponse> responseList = wishlistItems.stream()
                .map(item-> WishlistResponse.builder()
                        .wishlistId(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getProductName())
                        .productImage(item.getProduct().getImageUrl())
                        .productPrice(item.getProduct().getPrice())
                        .build())
                .collect(Collectors.toList());
        return responseList;
    }

    public String removeProductFromWishlist(String email, int productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new RuntimeException("User not found"));
        
        List<Wishlist> wishlistItems = wishlistRepository.findByUserId(user.getId());

        Wishlist wishlistItem = wishlistItems.stream()
                .filter(item -> item.getProduct().getId()==productId)
                .findFirst()
                .orElseThrow(()-> new RuntimeException("Product not found in wishlist"));

        wishlistRepository.delete(wishlistItem);
        return "Product removed from wishlist successfully!";
    }
}
