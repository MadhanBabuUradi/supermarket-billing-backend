package com.supermarket.service;

import com.supermarket.model.Product;  // Import model Product
import java.util.List;

public interface ProductService {

    Product createProduct(Product product);

    Product updateProduct(Long id, Product product);

    Product getProductById(Long productId);

    Product getProductByCode(String code);

    List<Product> getAllProducts();

    List<Product> searchProductsByName(String name);

    List<Product> getProductsByCategory(String category);

    List<Product> getLowStockProducts(Integer threshold);

    void deleteProduct(Long id);

    Product updateProductQuantity(Long id, Integer change);
}
