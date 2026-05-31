package com.ocofruit.oco.Repository;

import com.ocofruit.oco.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Tìm theo tên (chứa từ khóa, không phân biệt hoa thường)
    List<Product> findByNameContainingIgnoreCase(String name);

    // Tìm theo danh mục
    List<Product> findByCategory(String category);

    // Tìm sản phẩm đang active
    List<Product> findByActiveTrue();

    // Tìm theo danh mục và đang active
    List<Product> findByCategoryAndActiveTrue(String category);

    // Tìm theo khoảng giá
    List<Product> findByPriceBetween(Double minPrice, Double maxPrice);

    // Custom query - tìm kiếm nâng cao
    @Query("SELECT p FROM Product p WHERE " +
       "(:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
       "(:category IS NULL OR p.category = :category) AND " +
       "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
       "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
       "p.active = true")
List<Product> searchProducts(
    @Param("name") String name,
    @Param("category") String category,
    @Param("minPrice") Double minPrice,
    @Param("maxPrice") Double maxPrice
);

    // Đếm sản phẩm theo danh mục
    long countByCategory(String category);
    
}