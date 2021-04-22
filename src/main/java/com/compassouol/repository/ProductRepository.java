/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.repository;

import com.compassouol.entity.Product;
import com.compassouol.model.SearchProduct;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, String> {
    
    @Query("select a from Product a "
            + "where ((lower(a.name) like lower(concat('%', :#{#search.q},'%')) "
            + "or lower(a.description) like lower(concat('%', :#{#search.q},'%'))) or :#{#search.q} is null) and ("
            + "(:#{#search.min_price} <> 0.0 and a.price >= :#{#search.min_price}) or"
            + "(:#{#search.max_price} <> 0.0 and a.price <= :#{#search.max_price}) or"
            + "(:#{#search.min_price} = 0.0 and :#{#search.max_price} = 0.0)"
            + ") and (:#{#search.q} is not null or :#{#search.min_price} <> 0.0 or :#{#search.max_price} <> 0.0)")
    public List<Product> search(@Param("search") SearchProduct searchProdct);
}
