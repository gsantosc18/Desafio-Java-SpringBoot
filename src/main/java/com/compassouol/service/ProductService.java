/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.service;

import com.compassouol.entity.Product;
import com.compassouol.model.SearchProduct;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    Optional<Product> save(Product product);
    Optional<Product> getById(String id);
    Product update(Product product);
    List<Product> findAll();
    List<Product> search(SearchProduct searchProduct);
    void delete(Product product);
}
