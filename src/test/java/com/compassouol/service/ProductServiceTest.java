/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.service;

import com.compassouol.entity.Product;
import com.compassouol.repository.ProductRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {
    @Autowired @Qualifier("productimp")
    ProductService service;
    
    @MockBean
    ProductRepository repository;
    
    private Product getProduct() {
        return 
            Product
                .builder()
                .name("Product Test")
                .description("New Product Test")
                .price(39.9)
                .build();
    }
    
    @Test
    @DisplayName("Deve salvar um produto.")
    public void saveNewProduct() throws Exception {
        Product product = getProduct();
        
        Product entity = 
                Product
                        .builder()
                        .id("123")
                        .name("Product Test")
                        .description("New Product Test")
                        .price(39.9)
                        .build();
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(entity);;
        
        Product savedProduct = service.save(product).get();
        
        assertThat(savedProduct.getId()).isNotEmpty();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
    }
    
    @Test
    @DisplayName("Busca um produto existente")
    public void shouldFindProduct() {
        String id = "1";
        Product product = 
                Product
                    .builder()
                    .id(id)
                    .name("Book Test")
                    .description("New Book Test")
                    .price(39.9)
                    .build();
        
        Mockito.when(repository.findById(Mockito.any(String.class)))
                .thenReturn(Optional.of(product));
        
        Product findProduct = service.getById(id).get();
        
        assertThat(findProduct).isInstanceOf(Product.class);
        assertThat(findProduct.getId()).isEqualTo(id);
        assertThat(findProduct.getName()).isEqualTo(product.getName());
        assertThat(findProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(findProduct.getPrice()).isEqualTo(product.getPrice());
    }
    
    @Test
    @DisplayName("Atualiza um produto existente")
    public void updateProduct() {
        Product product = 
                Product
                        .builder()
                        .id("123")
                        .name("Product Test")
                        .description("New Product Test")
                        .price(39.9)
                        .build();
        Product entity = 
                Product
                        .builder()
                        .id("123")
                        .name("Product Test")
                        .description("New Product Test")
                        .price(39.9)
                        .build();
        Mockito.when(repository.save(Mockito.any(Product.class))).thenReturn(entity);
        
        Product updatedProduct = service.update(product);
        
        assertThat(updatedProduct).isInstanceOf(Product.class);
        assertThat(updatedProduct.getId()).isEqualTo(product.getId());
        assertThat(updatedProduct.getName()).isEqualTo(product.getName());
        assertThat(updatedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(updatedProduct.getPrice()).isEqualTo(product.getPrice());
    }
    
    @Test
    @DisplayName("Retorna uma lista de produtos")
    public void getProducts() {
        List<Product> products = Arrays.asList(getProduct(),getProduct(),getProduct());
        Mockito.when(repository.findAll()).thenReturn(products);
        
        List<Product> updatedProduct = service.findAll();
        
        assertThat(updatedProduct).isInstanceOf(List.class);
        assertThat(updatedProduct.size()).isEqualTo(3);
    }
}
