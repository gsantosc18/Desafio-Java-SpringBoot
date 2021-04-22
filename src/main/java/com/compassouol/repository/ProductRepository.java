/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.repository;

import com.compassouol.entity.Product;
import com.compassouol.model.QuerySearchProduct;
import com.compassouol.model.SearchProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, String> {
    
    @Query(QuerySearchProduct.SEARCH)
    public List<Product> search(@Param("search") SearchProduct searchProdct);
}
