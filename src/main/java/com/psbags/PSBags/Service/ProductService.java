package com.psbags.PSBags.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.psbags.PSBags.DTO.requests.ProductRequests;
import com.psbags.PSBags.DTO.response.ProductResponse;
import com.psbags.PSBags.Exception.CustomException;
import com.psbags.PSBags.Model.Product;
import com.psbags.PSBags.Model.User;
import com.psbags.PSBags.Repo.ProductRepo;
import com.psbags.PSBags.Repo.UserRepo;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CloudinaryService cloudinaryService;


    public ProductResponse addProduct(ProductRequests productRequests, String email) throws IOException {
        User user = this.userRepo.findByEmail(email).orElseThrow();

        Product products = new Product();
        LocalDateTime now = LocalDateTime.now();

        products.setProductName(productRequests.getProductName());
        products.setPrice(productRequests.getPrice());
        products.setQuantity(productRequests.getQuantity());
        products.setDescription(productRequests.getDescription());
        products.setXS(productRequests.getXS());
        products.setM(productRequests.getM());
        // products.setActive(products.isActive());
        products.setIsActive(productRequests.getIsActive());
        products.setOriginalPrice(productRequests.getOriginalPrice());
        products.setDiscount(productRequests.getDiscount());

        products.setL(productRequests.getL());
        products.setXL(productRequests.getXL());
        products.setXXL(productRequests.getXXL());
        products.setCategory(productRequests.getCategory());
if (productRequests.getImage() != null && !productRequests.getImage().isEmpty()) {
            Map uploadResult = cloudinaryService.upload(productRequests.getImage());
            products.setImageUrl(uploadResult.get("secure_url").toString());
            products.setImagePublicId(uploadResult.get("public_id").toString());
        }
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = now.format(dateFormatter);
        products.setDate(date);

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time = now.format(timeFormatter);

        products.setTime(time);
        productRepo.save(products);

        return new ProductResponse(products.getId(), "Product Added Successfully", products.getProductName());

    }


public ProductResponse updateProduct(int productId, ProductRequests request, String email) throws IOException {
        User user = this.userRepo.findByEmail(email).orElseThrow();

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with id " + productId));

        product.setProductName(request.getProductName());
        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());
        product.setIsActive(request.getIsActive());
        product.setDescription(request.getDescription());
        product.setXS(request.getXS());
        product.setM(request.getM());
        product.setL(request.getL());
        product.setXL(request.getXL());
        product.setXXL(request.getXXL());
        product.setCategory(request.getCategory());
        product.setDate(request.getDate());
        product.setTime(request.getTime());
        product.setDiscount(request.getDiscount());
        product.setOriginalPrice(request.getOriginalPrice());


        if (request.getImage() != null && !request.getImage().isEmpty()) {
            if (product.getImagePublicId() != null) {
                cloudinaryService.delete(product.getImagePublicId());
            }

            Map uploadResult = cloudinaryService.upload(request.getImage());
            product.setImageUrl(uploadResult.get("secure_url").toString());
            product.setImagePublicId(uploadResult.get("public_id").toString());
        }

        productRepo.save(product);

        return new ProductResponse(
                product.getId(),
                product.getProductName(),
                "Product updated successfully!"
        );
    }



    public ProductResponse deleteProduct(int id, String email) throws IOException {
        User user = this.userRepo.findByEmail(email).orElseThrow();

        if (!productRepo.existsById(id)) {
            return new ProductResponse(id, null, "Product not found!");
        }

        Product product = productRepo.getReferenceById(id);

        if (product.getImagePublicId() != null && !product.getImagePublicId().isEmpty()) {
            cloudinaryService.delete(product.getImagePublicId());
        }

        productRepo.delete(product);
        return new ProductResponse(id, product.getProductName(), "Product deleted successfully!");
    }


    
    public List<Product> getAllProducts() {
        // User user = this.userRepo.findByEmail(email);

        return productRepo.findAll();
    }

    public List<Product> getProductsByCategory(String category) {
        // User user = this.userRepo.findByEmail(email);

        return productRepo.findAll().stream().filter(product -> product.getCategory().equalsIgnoreCase(category))
                .toList();
    }

    public List<Product> getLatestProducts() {
        // User user = this.userRepo.findByEmail(email);

        return productRepo.findTop4ByOrderByDateDescTimeDesc();
    }

    public Product getByIdProductId(int id){
        return this.productRepo.findById(id).orElseThrow(()->new CustomException("Product not found with that Id"+id));
    }


    

}
