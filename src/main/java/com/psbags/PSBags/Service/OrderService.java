package com.psbags.PSBags.Service;

import com.psbags.PSBags.DTO.requests.BuyNowRequest;
import com.psbags.PSBags.DTO.response.OrderResponse;
import com.psbags.PSBags.DTO.response.UserWithOrderStatsDTO;
import com.psbags.PSBags.DTO.response.AdminOrderResponse;
import com.psbags.PSBags.DTO.response.DashboardResponse;
import com.psbags.PSBags.Model.Address;
import com.psbags.PSBags.Model.Cart;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Model.UserOrders;
import com.psbags.PSBags.Repo.AddressRepo;
import com.psbags.PSBags.Repo.ProductRepo;
import com.psbags.PSBags.Repo.UserRepo;
import com.psbags.PSBags.Repo.UserOrdersRepo;
import lombok.RequiredArgsConstructor;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

// Razorpay
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.psbags.PSBags.DTO.requests.RazorpayOrderRequest;
import com.psbags.PSBags.DTO.requests.RazorpayPaymentVerificationRequest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final UserRepo userRepository;
    private final ProductRepo productRepository;
    private final UserOrdersRepo orderRepository;
    private final CartService cartService;

    @Autowired
    private AddressRepo addresssRepo;

    @Value("${razorpay.key.id}")
    private String razorpayKeyId;

    @Value("${razorpay.key.secret}")
    private String razorpayKeySecret;

    @Autowired
    private ShiprocketService shiprocketService;

    @Transactional
    public OrderResponse buyNow(Integer userId, BuyNowRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getIsActive()=="0") {
            throw new RuntimeException("Product is not available");
        }

        if (product.getQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock available");
        }

        double price;
        try {
            price = Double.parseDouble(product.getPrice());
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid product price format");
        }
        double totalAmount = price * request.getQuantity();

        product.setQuantity(product.getQuantity() - request.getQuantity());
        productRepository.save(product);

        UserOrders order = new UserOrders();
        order.setUser(user);
        order.setProductName(product.getProductName());
        order.setQuantity(request.getQuantity());
        order.setSize(request.getSize());
        order.setImage(product.getImageUrl());
        order.setTotalAmount(totalAmount);
        order.setStatus("CONFIRMED");
        order.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        UserOrders savedOrder = orderRepository.save(order);

        return new OrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderDate(),
                savedOrder.getTotalAmount(),
                savedOrder.getStatus(),
                savedOrder.getProductName(),
                savedOrder.getQuantity(),
                savedOrder.getSize(),
                savedOrder.getImage(),
                user.getId(),
                "Order placed successfully"
        );
    }

    @Transactional
    public OrderResponse checkoutCart(Integer userId, int addressId) throws Exception { 
        Cart cart = cartService.getOrCreateCart(userId);
        Address address = addresssRepo.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found with ID: " + addressId));


        if (cart.getItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        User user = cart.getUser();
        double totalAmount = 0;
        int totalQuantity = 0;
        int itemCount = cart.getItems().size();

        for (var item : cart.getItems()) {
            Product product = item.getProduct();

            if ("0".equals(product.getIsActive())) {
                throw new RuntimeException("Product " + product.getProductName() + " is no longer available");
            }

            if (product.getQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getProductName());
            }

            double price;
            try {
                price = Double.parseDouble(product.getPrice());
            } catch (NumberFormatException e) {
                throw new RuntimeException("Invalid price format for product: " + product.getProductName());
            }
            totalAmount += price * item.getQuantity();
            totalQuantity += item.getQuantity();
        }


        for (var item : cart.getItems()) {
            Product product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        List<UserOrders> savedOrders = new ArrayList<>();
        for (var item : cart.getItems()) {
            Product product = item.getProduct();
            double price = Double.parseDouble(product.getPrice());
            double lineTotal = price * item.getQuantity();

            UserOrders lineOrder = new UserOrders();
            lineOrder.setUser(user);
            lineOrder.setProductName(product.getProductName());
            lineOrder.setQuantity(item.getQuantity());
            lineOrder.setSize(item.getSize() == null ? "NA" : item.getSize());
            lineOrder.setImage(product.getImageUrl());
            lineOrder.setTotalAmount(lineTotal);
            lineOrder.setStatus("CONFIRMED");
            lineOrder.setOrderDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            lineOrder.setPaymentStatus("SUCCESS");
            lineOrder.setPaymentType("RAZORPAY");
            lineOrder.setAddress(address);
            UserOrders persisted = orderRepository.save(lineOrder);
            savedOrders.add(persisted);
        }

        try {
            String paymentTypeForDbAndShiprocket = "RAZORPAY";
            java.util.List<String> productNames = new java.util.ArrayList<>();
            java.util.List<Integer> quantities = new java.util.ArrayList<>();
            java.util.List<Double> prices = new java.util.ArrayList<>();

            for (var item : cart.getItems()) {
                Product p = item.getProduct();
                productNames.add(p.getProductName());
                quantities.add(item.getQuantity());
                prices.add(Double.parseDouble(p.getPrice())); 
            }

            String shiprocketResp = shiprocketService.createOrder(
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail(),
                    user.getPhoneNumber(),
                    address.getAddress(),
                    address.getCity(),
                    address.getState(),
                    address.getCountry(),
                    address.getPincode(),
                    productNames,
                    paymentTypeForDbAndShiprocket,
                    quantities,
                    prices
            );
            System.out.println("Shiprocket create order response: " + shiprocketResp);

            try {
                Long shipOrderId = null;
                Long shipmentIdVal = null;
                JSONObject resp = new JSONObject(shiprocketResp);
                if (resp.has("order_id") && !resp.isNull("order_id")) {
                    shipOrderId = resp.optLong("order_id");
                }
                if (resp.has("shipment_id") && !resp.isNull("shipment_id")) {
                    shipmentIdVal = resp.optLong("shipment_id");
                }
                if ((shipOrderId == null || shipmentIdVal == null) && resp.has("data") && !resp.isNull("data")) {
                    JSONObject data = resp.optJSONObject("data");
                    if (data != null) {
                        if (shipOrderId == null && data.has("order_id") && !data.isNull("order_id")) {
                            shipOrderId = data.optLong("order_id");
                        }
                        if (shipmentIdVal == null && data.has("shipment_id") && !data.isNull("shipment_id")) {
                            shipmentIdVal = data.optLong("shipment_id");
                        }
                    }
                }

                if (shipOrderId != null || shipmentIdVal != null) {
                    for (UserOrders o : savedOrders) {
                        if (shipOrderId != null) o.setShiprocketOrderId(shipOrderId);
                        if (shipmentIdVal != null) o.setShipmentId(shipmentIdVal);
                        orderRepository.save(o);
                    }
                }
            } catch (Exception parseEx) {
                System.err.println("Failed to parse Shiprocket response for storing IDs: " + parseEx.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Failed to create Shiprocket order: " + e.getMessage());
        }

        cartService.clearCart(userId);

        return new OrderResponse(
                0,
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                totalAmount,
                "CONFIRMED",
                "MULTIPLE ITEMS (" + itemCount + ")",
                totalQuantity,
                "MIXED",
                null,
                user.getId(),
                "Order placed successfully from cart with " + itemCount + " items"
        );
    }

    public Map<String, Object> createRazorpayOrder(RazorpayOrderRequest request, Integer userId) throws RazorpayException {
        if (request == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid amount for Razorpay order");
        }

        RazorpayClient razorpay = new RazorpayClient(razorpayKeyId, razorpayKeySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int) (request.getAmount() * 100)); 
        orderRequest.put("currency", request.getCurrency() == null ? "INR" : request.getCurrency());
        orderRequest.put("receipt", request.getReceipt() == null ? ("rcpt_" + System.currentTimeMillis()) : request.getReceipt());
        orderRequest.put("payment_capture", 1);

        com.razorpay.Order rpOrder = razorpay.orders.create(orderRequest);

        Map<String, Object> response = new HashMap<>();
        response.put("id", rpOrder.get("id"));
        response.put("amount", rpOrder.get("amount"));
        response.put("currency", rpOrder.get("currency"));
        response.put("receipt", rpOrder.get("receipt"));
        response.put("status", rpOrder.get("status"));
        return response;
    }
    

    @Transactional
    public OrderResponse verifyAndSaveOrder(RazorpayPaymentVerificationRequest request, Integer userId, int addressId) throws Exception {
        if (request == null || request.getRazorpayOrderId() == null || request.getRazorpayPaymentId() == null || request.getRazorpaySignature() == null) {
            throw new RuntimeException("Incomplete payment verification payload");
        }

        String data = request.getRazorpayOrderId() + "|" + request.getRazorpayPaymentId();
        String computed = hmacSha256(data, razorpayKeySecret);
        if (!computed.equals(request.getRazorpaySignature())) {
            throw new RuntimeException("Invalid payment signature");
        }

        return checkoutCart(userId, addressId);
    }

    private String hmacSha256(String data, String secret) {
        try {
            Mac sha256Hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256Hmac.init(secretKey);
            byte[] signedBytes = sha256Hmac.doFinal(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : signedBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to compute signature: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getPurchaseHistory(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findByUserOrderByIdDesc(user).stream()
                .map(order -> new OrderResponse(
                        order.getId(),
                        order.getOrderDate(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getSize(),
                        order.getImage(),
                        user.getId(),
                        ""
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
public List<AdminOrderResponse> getAllOrdersForAdmin(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow();

    return orderRepository.findAll().stream()
            .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate())) 
            .map(order -> {
                User u = order.getUser();
                return new AdminOrderResponse(
                        order.getId(),
                        order.getOrderDate(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getProductName(),
                        order.getQuantity(),
                        order.getSize(),
                        order.getImage(),
                        u != null ? u.getId() : 0,
                        u != null ? u.getFirstName() : null,
                        u != null ? u.getLastName() : null,
                        u != null ? u.getEmail() : null,
                        u != null ? u.getPhoneNumber() : null
                );
            })
            .collect(Collectors.toList());
}


    public UserOrders updateStatus(Integer orderId, String newStatus, String email) { 
        UserOrders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    // public AdminOrderResponse getOrderById(int orderId){
    // UserOrders order = orderRepository.findById(orderId).orElseThrow();
    // return new AdminOrderResponse(order.getId(),
    // order.getOrderDate(),
    // order.getTotalAmount(),
    // order.getStatus(),
    // order.getProductName(),
    // order.getQuantity(),
    // order.getSize(),order.getImage(),
    // order.getUser().getId(),
    // order.getUser().getFirstName(),
    // order.getUser().getLastName(),
    // order.getUser().getEmail(),
    // order.getUser().getPhoneNumber());


    public AdminOrderResponse getOrderById(int orderId, String email){
        User user = this.userRepository.findByEmail(email).orElseThrow();
    UserOrders order = orderRepository.findById(orderId).orElseThrow();
    return new AdminOrderResponse(order.getId(),
    order.getOrderDate(),
    order.getTotalAmount(),
    order.getStatus(),
    order.getProductName(),
    order.getQuantity(),
    order.getSize(),order.getImage(),
    order.getUser().getId(),
    order.getUser().getFirstName(),
    order.getUser().getLastName(),
    order.getUser().getEmail(),
    order.getUser().getPhoneNumber());
       
        
}





    public List<UserWithOrderStatsDTO> getAllUserByRole(String email) {
        User user = this.userRepository.findByEmail(email).orElseThrow();

    List<User> users = userRepository.findAll().stream()
            .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
            .toList();

    List<UserOrders> orders = orderRepository.findAll();

    return users.stream().map(u -> {
        List<UserOrders> userOrders = orders.stream()
                .filter(o -> o.getUser().getId() == u.getId())
                .toList();

        long pending = userOrders.stream().filter(o -> "PENDING".equalsIgnoreCase(o.getStatus())).count();
        long processing = userOrders.stream().filter(o -> "PROCESSING".equalsIgnoreCase(o.getStatus())).count();
        long shipped = userOrders.stream().filter(o -> "SHIPPED".equalsIgnoreCase(o.getStatus())).count();
        long delivered = userOrders.stream().filter(o -> "DELIVERED".equalsIgnoreCase(o.getStatus())).count();
        long cancelled = userOrders.stream().filter(o -> "CANCELLED".equalsIgnoreCase(o.getStatus())).count();

        return new UserWithOrderStatsDTO(
                u.getId(),
                u.getFirstName(),
                u.getLastName(),
                u.getEmail(),
                u.getRole(),
                u.getPhoneNumber(),
                pending,
                processing,
                shipped,
                delivered,
                cancelled
        );
    }).toList();
}

public DashboardResponse countForDashboard(String email) {
    User user = this.userRepository.findByEmail(email).orElseThrow();
        int customerCount = (int) userRepository.findAll()
                .stream()
                .filter(u -> "USER".equalsIgnoreCase(u.getRole()))
                .count();

        int orderCount = orderRepository.findAll().size();

        int productCount = productRepository.findAll().size();

        double totalPrice = orderRepository.findAll()
                .stream()
                .filter(o->o.getStatus().equals("DELIVERED"))
                .mapToDouble(UserOrders::getTotalAmount)   
                .sum();

        return new DashboardResponse(orderCount, (int) totalPrice, productCount, customerCount);
    }

    


}
