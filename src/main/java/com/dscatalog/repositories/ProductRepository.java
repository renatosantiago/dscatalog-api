package com.dscatalog.repositories;

import java.util.List;

import com.dscatalog.entities.Category;
import com.dscatalog.entities.Product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {
  
  @Query("SELECT DISTINCT p FROM Product p "
          + "INNER JOIN p.categories p_categories "
          + "WHERE "
          + "(COALESCE(:categories) IS NULL OR p_categories IN :categories) "
          + "AND (LOWER(p.name) LIKE LOWER(CONCAT('%',:name,'%')) )")
  Page<Product> search(List<Category> categories, String name, Pageable pageable);

  @Query("SELECT product FROM Product product " 
          + " JOIN FETCH product.categories "
          + " WHERE product IN :products")
  List<Product> findProductWithCategories(List<Product> products);
}
