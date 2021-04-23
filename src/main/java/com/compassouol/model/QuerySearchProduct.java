/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.model;

/**
 *
 * @author 320099352
 */
public class QuerySearchProduct {
    public static final String SEARCH = "select a from Product a "
            + "where ((lower(a.name) like lower(concat('%', :#{#search.q},'%')) "
            + "or lower(a.description) like lower(concat('%', :#{#search.q},'%'))) or :#{#search.q} is null) and ("
            + "(:#{#search.min_price} <> 0.0 and :#{#search.max_price} = 0.0 and a.price >= :#{#search.min_price}) or"
            + "(:#{#search.max_price} <> 0.0 and :#{#search.min_price} = 0.0 and a.price <= :#{#search.max_price}) or"
            + "(:#{#search.max_price} <> 0.0 and :#{#search.min_price} <> 0.0 and a.price between :#{#search.min_price} and :#{#search.max_price}) or"
            + "(:#{#search.min_price} = 0.0 and :#{#search.max_price} = 0.0)"
            + ") and (:#{#search.q} is not null or :#{#search.min_price} <> 0.0 or :#{#search.max_price} <> 0.0)";
}
