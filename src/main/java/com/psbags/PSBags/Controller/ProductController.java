package com.psbags.PSBags.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/latest-by-subcategory")
    public List<Product> getLatestProductsBySubcategory(@RequestParam(required = false) String category) {
        return productService.getLatestProductsBySubcategory(category);
    }
}
