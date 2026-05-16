package com.psbags.PSBags.Service;

import com.psbags.PSBags.Model.Cart;
import com.psbags.PSBags.Model.CartItem;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.DTO.response.CartResponse;
import com.psbags.PSBags.DTO.response.CartItemResponse;

import com.psbags.PSBags.Repo.CartItemRepository;
import com.psbags.PSBags.Repo.CartRepository;
import com.psbags.PSBags.Repo.ProductRepo;
import com.psbags.PSBags.Repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepo productRepository;
    private final UserRepo userRepository;

    // for creating or getting the cart, we will find if the cart is present for that user if yes then we will fetch that cart if not
    // then we will assign him newCart
    // remember this is helper method for get or create a cart whi will use full in below apis
    @Transactional
    public Cart getOrCreateCart(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setItems(new ArrayList<>());
                    return cartRepository.save(cart);
                });
    }


    @Transactional
    public CartResponse addProductToCart(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Validate product is active
        if (product.getIsActive()=="0") {
            throw new RuntimeException("Product is not available");
        }

        // Validate stock
        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock available");
        }

        // check if product already exists in cart
        CartItem existingItem = cart.getItems()
                .stream()
                .filter(item -> Integer.valueOf(item.getProduct().getId()).equals(productId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (product.getQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock for requested quantity");
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            CartItem newItem = new CartItem();
            newItem.setQuantity(quantity);
            newItem.setSize(null); // size can be set later via updateProductSize API
            newItem.setCart(cart);
            newItem.setProduct(product);
            cart.addItem(newItem);
            cartItemRepository.save(newItem);
        }

        cartRepository.save(cart);
        return convertToCartResponse(cart);
    }


    public CartResponse getCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        return convertToCartResponse(cart);
    }

    @Transactional
    public CartResponse removeProductFromCart(Integer userId, Integer productId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().removeIf(item -> Integer.valueOf(item.getProduct().getId()).equals(productId));
        cartRepository.save(cart);
        return convertToCartResponse(cart);
    }

    @Transactional
    public CartResponse updateProductQuantity(Integer userId, Integer productId, int quantity) {
        Cart cart = getOrCreateCart(userId);
        
        if (quantity <= 0) {
            throw new RuntimeException("Quantity must be greater than 0");
        }

        cart.getItems().forEach(item -> {
            if (Integer.valueOf(item.getProduct().getId()).equals(productId)) {
                // Validate stock
                if (item.getProduct().getQuantity() < quantity) {
                    throw new RuntimeException("Insufficient stock for requested quantity");
                }
                item.setQuantity(quantity);
                cartItemRepository.save(item);
            }
        });

        cartRepository.save(cart);
        return convertToCartResponse(cart);
    }

    @Transactional
    public void clearCart(Integer userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    // Helper method to convert Cart entity to CartResponse DTO
    private CartResponse convertToCartResponse(Cart cart) {
        if (cart == null || cart.getUser() == null) {
            throw new RuntimeException("Invalid cart or user");
        }

        List<CartItemResponse> itemResponses = cart.getItems() == null ? 
                new ArrayList<>() : 
                cart.getItems().stream()
                        .map(this::convertToCartItemResponse)
                        .collect(Collectors.toList());

        double totalAmount = itemResponses.stream()
                .mapToDouble(CartItemResponse::getLineTotal)
                .sum();

        int totalItems = itemResponses.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        return new CartResponse(
                cart.getId(),
                cart.getUser().getId(),
                itemResponses,
                totalAmount,
                totalItems
        );
    }

    // Helper method to convert CartItem entity to CartItemResponse DTO
    private CartItemResponse convertToCartItemResponse(CartItem item) {
        if (item == null || item.getProduct() == null) {
            throw new RuntimeException("Invalid cart item or product");
        }
        
        Product product = item.getProduct();
        double price;
        try {
            price = Double.parseDouble(product.getPrice());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid product price format");
        }
        double lineTotal = price * item.getQuantity();

        return new CartItemResponse(
                item.getId(),
                item.getQuantity(),
                lineTotal,
                item.getSize(),
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getCategory(),
                product.getIsActive()
        );
    }

    @Transactional
    public CartResponse updateProductSize(Integer userId, Integer productId, String size) {
        Cart cart = getOrCreateCart(userId);
        boolean updated = false;

        for (CartItem item : cart.getItems()) {
            if (Integer.valueOf(item.getProduct().getId()).equals(productId)) {
                item.setSize(size);
                cartItemRepository.save(item);
                updated = true;
                break;
            }
        }

        if (!updated) {
            throw new RuntimeException("Product not found in cart");
        }

        cartRepository.save(cart);
        return convertToCartResponse(cart);
    }
}
