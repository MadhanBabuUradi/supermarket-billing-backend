package com.supermarket.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.supermarket.exception.ResourceNotFoundException;
import com.supermarket.model.Product;
import com.supermarket.repository.ProductRepository;
import com.supermarket.service.ProductService;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        if (productRepository.existsByCode(product.getCode())) {
            throw new IllegalArgumentException("Product with code " + product.getCode() + " already exists");
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));

        if (!existingProduct.getCode().equals(product.getCode())
                && productRepository.existsByCode(product.getCode())) {
            throw new IllegalArgumentException("Product with code " + product.getCode() + " already exists");
        }

        existingProduct.setName(product.getName());
        existingProduct.setCode(product.getCode());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setTaxPercentage(product.getTaxPercentage());

        return productRepository.save(existingProduct);
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
    }

    @Override
    public Product getProductByCode(String code) {
        return productRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "code", code));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findByQuantityLessThan(threshold);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id));
        productRepository.delete(product);
    }

    @Override
    @Transactional
    public Product updateProductQuantity(Long id, Integer quantityChange) {
        Product product = getProductById(id);

        int newQuantity = product.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Insufficient stock for product: " + product.getName()
                    + ". Available: " + product.getQuantity() + ", Requested change: " + (-quantityChange));
        }

        product.setQuantity(newQuantity);
        return productRepository.save(product);
    }
}
