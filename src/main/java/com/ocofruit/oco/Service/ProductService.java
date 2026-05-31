package com.ocofruit.oco.Service;

import com.ocofruit.oco.Model.Product;
import com.ocofruit.oco.Repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByActiveTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));

        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setQuantity(productDetails.getQuantity());
        product.setImageUrl(productDetails.getImageUrl());
        product.setCategory(productDetails.getCategory());

        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm ID: " + id));
        product.setActive(false); // Soft delete
        productRepository.save(product);
    }

    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return productRepository.searchProducts(name, category, minPrice, maxPrice);
    }

    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategoryAndActiveTrue(category);
    }
}