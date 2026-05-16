package com.psbags.PSBags.Controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;

import com.psbags.PSBags.DTO.requests.ProductRequests;
import com.psbags.PSBags.DTO.response.ProductResponse;
import com.psbags.PSBags.DTO.response.UserWithOrderStatsDTO;
import com.psbags.PSBags.DTO.response.AdminOrderResponse;
import com.psbags.PSBags.DTO.response.DashboardResponse;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Model.UserOrders;
import com.psbags.PSBags.Service.ProductService;
import com.psbags.PSBags.Service.OrderService;

@RestController
@RequestMapping("/admin")
public class  AdminController {
    
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @PostMapping(value = "/addProduct", consumes = "multipart/form-data")
    public ProductResponse addProduct(@ModelAttribute ProductRequests productRequests) throws IOException {
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return productService.addProduct(productRequests,email);
    }

    @PutMapping("/updateProduct/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable int id,
            @ModelAttribute ProductRequests request) throws IOException {

              Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();

        ProductResponse response = productService.updateProduct(id, request,email);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/deleteProduct/{id}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable int id) throws IOException {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return ResponseEntity.ok(productService.deleteProduct(id,email));
    }

    // @GetMapping("/getAllProducts")
    // public List<Product> getAllProducts(){
    //      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 		String email = authentication.getName();
    //     return productService.getAllProducts();
    // }

    // @GetMapping("/getProductByCategory")
    // public List<Product> getAllProductsByCategory(@RequestParam String category){ 
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    // 		String email = authentication.getName();
    //     return this.productService.getProductsByCategory(category);
    // }

    // @GetMapping("/getLatestProducts")
    // public List<Product> getLatestProducts(){
    //   //   Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 	// String email = authentication.getName();
    //     return this.productService.getLatestProducts();
    // }

    @GetMapping("/orders")
    public ResponseEntity<List<AdminOrderResponse>> getAllOrders() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        List<AdminOrderResponse> orders = orderService.getAllOrdersForAdmin(email);
        return ResponseEntity.ok(orders);
    }


    @PutMapping("/update-status/{orderId}")
    public ResponseEntity<UserOrders> updateStatus(
            @PathVariable Integer orderId,
            @RequestParam String newStatus) {
              Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		    String email = authentication.getName();
                UserOrders updatedOrder = orderService.updateStatus(orderId, newStatus,email);
                return ResponseEntity.ok(updatedOrder);
              
            }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<AdminOrderResponse> getOrderById(@PathVariable int orderId) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		    String email = authentication.getName();
            return ResponseEntity.ok(orderService.getOrderById(orderId,email));
    }



    @GetMapping("/role-stats")
    public ResponseEntity<List<UserWithOrderStatsDTO>> getAllUserByRole() {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        List<UserWithOrderStatsDTO> response = orderService.getAllUserByRole(email);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/dashboardResponse")
    public DashboardResponse getAllDashboardCount(){
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    		String email = authentication.getName();
        return this.orderService.countForDashboard(email);
    }
}
