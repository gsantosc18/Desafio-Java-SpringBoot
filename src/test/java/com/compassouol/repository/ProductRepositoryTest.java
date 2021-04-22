/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.repository;

import com.compassouol.entity.Product;
import com.compassouol.model.SearchProduct;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureDataJpa
public class ProductRepositoryTest {
    @Autowired
    ProductRepository repository;
    
    @Test
    @DisplayName("Deve salvar um produto")
    public void createNewProduct() {
        Product product = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(39.9)
                        .build();
        
        Product savedProduct = repository.save(product);
        
        assertThat(savedProduct).isInstanceOf(Product.class);
        assertThat(savedProduct.getId()).isNotEmpty();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
    }
    
    @Test
    @DisplayName("Deve retornar o resultado da busca com parâmetro")
    public void findProductBySearchObject() {
        SearchProduct searchProdct = SearchProduct.builder().q("Book 2 Test 2").build();
        
        Product product = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(39.9)
                        .build();
        Product product2 = 
                Product
                        .builder()
                        .name("Book Test 2")
                        .description("New Book 2 Test 2")
                        .price(19.9)
                        .build();
        repository.save(product);
        repository.save(product2);
        
        List<Product> products = repository.search(searchProdct);
        
        assertThat(products).isInstanceOf(List.class);
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0).getName()).isEqualTo(product2.getName());
        assertThat(products.get(0).getDescription()).isEqualTo(product2.getDescription());
        assertThat(products.get(0).getPrice()).isEqualTo(product2.getPrice());
    }
    
    @Test
    @DisplayName("Deve retornar o resultado da busca com parâmetro de preço minimo")
    public void findProductByMinPrice() {
        SearchProduct searchProdct = SearchProduct.builder().min_price(20).build();
        
        Product product = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(39.9)
                        .build();
        Product product2 = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(19.9)
                        .build();
        Product product3 = 
                Product
                        .builder()
                        .name("Book Test 2")
                        .description("New Book Test 2")
                        .price(19.9)
                        .build();
        repository.save(product);
        repository.save(product2);
        repository.save(product3);
        
        List<Product> products = repository.search(searchProdct);
        
        assertThat(products).isInstanceOf(List.class);
        assertThat(products.size()).isEqualTo(1);
        assertThat(products.get(0).getName()).isEqualTo(product.getName());
        assertThat(products.get(0).getDescription()).isEqualTo(product.getDescription());
        assertThat(products.get(0).getPrice()).isEqualTo(product.getPrice());
    }
    
    @Test
    @DisplayName("Deve retornar o resultado da busca com parâmetro de preço máximo")
    public void findProductByMaxPrice() {
        SearchProduct searchProdct = SearchProduct.builder().max_price(19.9).build();
        
        Product product = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(39.9)
                        .build();
        Product product2 = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(19.9)
                        .build();
        Product product3 = 
                Product
                        .builder()
                        .name("Book Test 2")
                        .description("New Book Test 2")
                        .price(19.9)
                        .build();
        repository.save(product);
        repository.save(product2);
        repository.save(product3);
        
        List<Product> products = repository.search(searchProdct);
        
        assertThat(products).isInstanceOf(List.class);
        assertThat(products.size()).isEqualTo(2);
        assertThat(products.get(0).getName()).isEqualTo(product2.getName());
        assertThat(products.get(0).getDescription()).isEqualTo(product2.getDescription());
        assertThat(products.get(0).getPrice()).isEqualTo(product2.getPrice());
    }
    
    @Test
    @DisplayName("Não deve retornar o resultado da busca com parâmetro")
    public void notFindProductWithQuery() {
        SearchProduct searchProdct = new SearchProduct();
        
        Product product = 
                Product
                        .builder()
                        .name("Book Test")
                        .description("New Book Test")
                        .price(39.9)
                        .build();
        repository.save(product);
        
        List<Product> products = repository.search(searchProdct);
        
        assertThat(products).isInstanceOf(List.class);
        assertThat(products.size()).isEqualTo(0);
    }
}
