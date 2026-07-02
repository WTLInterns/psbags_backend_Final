package com.psbags.PSBags.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
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
        products.setSubcategoryName(productRequests.getSubcategoryName());
        products.setShippingType(productRequests.getShippingType() != null ? productRequests.getShippingType() : "FREE");
        products.setShippingCost("PAID".equalsIgnoreCase(productRequests.getShippingType()) && productRequests.getShippingCost() != null ? productRequests.getShippingCost() : 0.0);

        // Parallel image uploads using CompletableFuture
        List<CompletableFuture<Void>> uploadFutures = new ArrayList<>();

        // Image 1 upload
        if (productRequests.getImage() != null && !productRequests.getImage().isEmpty()) {
            CompletableFuture<Void> image1Future = CompletableFuture.supplyAsync(() -> {
                try {
                    return cloudinaryService.upload(productRequests.getImage());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image 1", e);
                }
            }).thenAccept(uploadResult -> {
                products.setImageUrl(uploadResult.get("secure_url").toString());
                products.setImagePublicId(uploadResult.get("public_id").toString());
            });
            uploadFutures.add(image1Future);
        }

        // Image 2 upload
        if (productRequests.getImage2() != null && !productRequests.getImage2().isEmpty()) {
            CompletableFuture<Void> image2Future = CompletableFuture.supplyAsync(() -> {
                try {
                    return cloudinaryService.upload(productRequests.getImage2());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image 2", e);
                }
            }).thenAccept(uploadResult2 -> {
                products.setImageUrl2(uploadResult2.get("secure_url").toString());
                products.setImagePublicId2(uploadResult2.get("public_id").toString());
            });
            uploadFutures.add(image2Future);
        }

        // Image 3 upload
        if (productRequests.getImage3() != null && !productRequests.getImage3().isEmpty()) {
            CompletableFuture<Void> image3Future = CompletableFuture.supplyAsync(() -> {
                try {
                    return cloudinaryService.upload(productRequests.getImage3());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image 3", e);
                }
            }).thenAccept(uploadResult3 -> {
                products.setImageUrl3(uploadResult3.get("secure_url").toString());
                products.setImagePublicId3(uploadResult3.get("public_id").toString());
            });
            uploadFutures.add(image3Future);
        }

        // Image 4 upload
        if (productRequests.getImage4() != null && !productRequests.getImage4().isEmpty()) {
            CompletableFuture<Void> image4Future = CompletableFuture.supplyAsync(() -> {
                try {
                    return cloudinaryService.upload(productRequests.getImage4());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image 4", e);
                }
            }).thenAccept(uploadResult4 -> {
                products.setImageUrl4(uploadResult4.get("secure_url").toString());
                products.setImagePublicId4(uploadResult4.get("public_id").toString());
            });
            uploadFutures.add(image4Future);
        }

        // Image 5 upload
        if (productRequests.getImage5() != null && !productRequests.getImage5().isEmpty()) {
            CompletableFuture<Void> image5Future = CompletableFuture.supplyAsync(() -> {
                try {
                    return cloudinaryService.upload(productRequests.getImage5());
                } catch (IOException e) {
                    throw new RuntimeException("Failed to upload image 5", e);
                }
            }).thenAccept(uploadResult5 -> {
                products.setImageUrl5(uploadResult5.get("secure_url").toString());
                products.setImagePublicId5(uploadResult5.get("public_id").toString());
            });
            uploadFutures.add(image5Future);
        }

        // Wait for all uploads to complete before saving to database
        try {
            CompletableFuture.allOf(uploadFutures.toArray(new CompletableFuture[0])).join();
        } catch (Exception e) {
            throw new IOException("One or more image uploads failed", e);
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
        product.setSubcategoryName(request.getSubcategoryName());
        product.setShippingType(request.getShippingType() != null ? request.getShippingType() : "FREE");
        product.setShippingCost("PAID".equalsIgnoreCase(request.getShippingType()) && request.getShippingCost() != null ? request.getShippingCost() : 0.0);
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
        if (request.getImage2() != null && !request.getImage2().isEmpty()) {
            if (product.getImagePublicId2() != null && !product.getImagePublicId2().isEmpty()) {
                cloudinaryService.delete(product.getImagePublicId2());
            }
            Map uploadResult2 = cloudinaryService.upload(request.getImage2());
            product.setImageUrl2(uploadResult2.get("secure_url").toString());
            product.setImagePublicId2(uploadResult2.get("public_id").toString());
        }
        if (request.getImage3() != null && !request.getImage3().isEmpty()) {
            if (product.getImagePublicId3() != null && !product.getImagePublicId3().isEmpty()) {
                cloudinaryService.delete(product.getImagePublicId3());
            }
            Map uploadResult3 = cloudinaryService.upload(request.getImage3());
            product.setImageUrl3(uploadResult3.get("secure_url").toString());
            product.setImagePublicId3(uploadResult3.get("public_id").toString());
        }
        if (request.getImage4() != null && !request.getImage4().isEmpty()) {
            if (product.getImagePublicId4() != null && !product.getImagePublicId4().isEmpty()) {
                cloudinaryService.delete(product.getImagePublicId4());
            }
            Map uploadResult4 = cloudinaryService.upload(request.getImage4());
            product.setImageUrl4(uploadResult4.get("secure_url").toString());
            product.setImagePublicId4(uploadResult4.get("public_id").toString());
        }
        if (request.getImage5() != null && !request.getImage5().isEmpty()) {
            if (product.getImagePublicId5() != null && !product.getImagePublicId5().isEmpty()) {
                cloudinaryService.delete(product.getImagePublicId5());
            }
            Map uploadResult5 = cloudinaryService.upload(request.getImage5());
            product.setImageUrl5(uploadResult5.get("secure_url").toString());
            product.setImagePublicId5(uploadResult5.get("public_id").toString());
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
        if (product.getImagePublicId2() != null && !product.getImagePublicId2().isEmpty()) {
            cloudinaryService.delete(product.getImagePublicId2());
        }
        if (product.getImagePublicId3() != null && !product.getImagePublicId3().isEmpty()) {
            cloudinaryService.delete(product.getImagePublicId3());
        }
        if (product.getImagePublicId4() != null && !product.getImagePublicId4().isEmpty()) {
            cloudinaryService.delete(product.getImagePublicId4());
        }
        if (product.getImagePublicId5() != null && !product.getImagePublicId5().isEmpty()) {
            cloudinaryService.delete(product.getImagePublicId5());
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

    public List<Product> getProductsBySubcategory(String subcategoryName) {
        return productRepo.findAll().stream()
                .filter(product -> product.getSubcategoryName() != null && 
                        product.getSubcategoryName().equalsIgnoreCase(subcategoryName))
                .toList();
    }

    public List<Product> getLatestProducts() {
        // User user = this.userRepo.findByEmail(email);

        return productRepo.findTop4ByOrderByDateDescTimeDesc();
    }

    public Product getByIdProductId(int id){
        return this.productRepo.findById(id).orElseThrow(()->new CustomException("Product not found with that Id"+id));
    }

    public List<Product> getLatestProductsBySubcategory(String category) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return productRepo.findAll().stream()
                .filter(product -> product.getSubcategoryName() != null && !product.getSubcategoryName().isEmpty())
                .filter(product -> category == null || category.isEmpty() || (product.getCategory() != null && product.getCategory().equalsIgnoreCase(category)))
                .collect(Collectors.groupingBy(Product::getSubcategoryName,
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(product -> 
                                        LocalDateTime.parse(product.getDate() + " " + product.getTime(), 
                                                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))),
                                Optional::get)))
                .values().stream()
                .collect(Collectors.toList());
    }

}
