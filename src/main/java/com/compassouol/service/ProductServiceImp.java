/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.service;

import com.compassouol.entity.Product;
import com.compassouol.model.SearchProduct;
import com.compassouol.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("productimp")
public class ProductServiceImp implements ProductService {
    @Autowired
    private ProductRepository repository;

    @Override
    public Optional<Product> save(Product product) {
        return Optional.of(repository.save(product));
    }

    @Override
    public Optional<Product> getById(String id) {
        return repository.findById(id);
    }

    @Override
    public Product update(Product product) {
        return repository.save(product);
    }

    @Override
    public List<Product> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Product> search(SearchProduct searchProduct) {
        return repository.search(searchProduct);
    }

    @Override
    public void delete(Product product) {
        repository.delete(product);
    }
    
}
