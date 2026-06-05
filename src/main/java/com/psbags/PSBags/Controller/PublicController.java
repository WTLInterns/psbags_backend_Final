package com.psbags.PSBags.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Service.ProductService;
import com.psbags.PSBags.Service.AnnouncementService;
import com.psbags.PSBags.DTO.response.AnnouncementResponse;

@RestController
@RequestMapping("/public")  
public class PublicController {


    @Autowired
    private ProductService productService;
    
    @Autowired
    private AnnouncementService announcementService;
    

     @GetMapping("/getAllProducts")
    public List<Product> getAllProducts(){
         
        return productService.getAllProducts();
    }

    @GetMapping("/getProductByCategory")
    public List<Product> getAllProductsByCategory(@RequestParam String category){ 
        
        return this.productService.getProductsByCategory(category);
    }

    @GetMapping("/getProductBySubcategory")
    public List<Product> getAllProductsBySubcategory(@RequestParam String subcategoryName){ 
        
        return this.productService.getProductsBySubcategory(subcategoryName);
    }

    @GetMapping("/getLatestProducts")
    public List<Product> getLatestProducts(){
        
        return this.productService.getLatestProducts();
    }

    @GetMapping("/getProductById/{id}")
    public Product getProductById(@PathVariable int id){
        return this.productService.getByIdProductId(id);
    }



    @GetMapping("/announcements")
    public ResponseEntity<AnnouncementResponse> getActiveAnnouncements() {
        AnnouncementResponse response = announcementService.getActiveAnnouncements();
        return ResponseEntity.ok(response);
    } 

    
}
