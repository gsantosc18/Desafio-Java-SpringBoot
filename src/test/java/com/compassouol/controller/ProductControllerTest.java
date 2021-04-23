/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compassouol.controller;

import com.compassouol.dto.ProductDTO;
import com.compassouol.entity.Product;
import com.compassouol.model.SearchProduct;
import com.compassouol.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTest {
    
    static String PRODUCT_API = "/products";
    
    @Autowired
    MockMvc mvc;
    
    @MockBean @Qualifier("productimp")
    ProductService service;
    
    private Product getProduct() {
        return 
            Product
                .builder()
                .id("123")
                .name("Product Test")
                .description("New Product Test")
                .price(39.9)
                .build();
    }
    
    private ProductDTO getProductDTO() {
        return 
            ProductDTO
                .builder()
                .name("Product Test")
                .description("New Product Test")
                .price(39.9)
                .build();
    }
    
    @Test
    @DisplayName("Deve salvar um produto com sucesso.")
    public void createProduct() throws Exception {
        ProductDTO dto = getProductDTO();        
        Product savedProduct = getProduct();
        
        String json = new ObjectMapper().writeValueAsString(dto);
        
        BDDMockito.given(service.save(Mockito.any(Product.class))).willReturn(Optional.of(savedProduct));
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(dto.getName()))
                .andExpect(jsonPath("description").value(dto.getDescription()))
                .andExpect(jsonPath("price").value(dto.getPrice()));
    }
    
    
    
    @Test
    @DisplayName("Não deve salvar produto com preço negativo")
    public void shouldNotSaveWithPriceNegetive() throws Exception {
        ProductDTO product = 
                ProductDTO
                    .builder()
                    .name(getProductDTO().getName())
                    .description(getProductDTO().getDescription())
                    .price(-19.9)
                    .build();
        String json = new ObjectMapper().writeValueAsString(product);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").isNotEmpty());
    }
    
    @Test
    @DisplayName("Não deve salvar se for invalido")
    public void shoulDoesNotSaveInvalid() throws Exception {
        ProductDTO product = new ProductDTO();
        
        String json = new ObjectMapper().writeValueAsString(product);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
                
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }
    
    @Test
    @DisplayName("Deve atualizar um produto existente")
    public void updateProduct() throws Exception {
        String id = "1";        
        String json = new ObjectMapper().writeValueAsString(getProductDTO());        
        BDDMockito.given(service.getById(Mockito.any(String.class))).willReturn(Optional.of(getProduct()));
        Product updated = Product
                .builder()
                .id("123")
                .name("Product Updated Test")
                .description("Updated Product Test")
                .price(109.9)
                .build();
        BDDMockito.given(service.update(Mockito.any(Product.class))).willReturn(updated);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(updated.getName()))
                .andExpect(jsonPath("description").value(updated.getDescription()))
                .andExpect(jsonPath("price").value(updated.getPrice()));
    }
    
    @Test
    @DisplayName("Não deve atualizar um produto inexistente")
    public void shoulDoesNotUpdateInexistentProduct() throws Exception {
        String id = "1";        
        String json = new ObjectMapper().writeValueAsString(getProductDTO());        
        BDDMockito.given(service.getById(Mockito.any(String.class))).willReturn(Optional.empty());
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Não deve atualizar um produto inválido")
    public void shouldDoesNotUpdateInvalidProduct() throws Exception {
        String id = "1";
        String json = new ObjectMapper().writeValueAsString(new ProductDTO());
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }
    
    @Test
    @DisplayName("Não deve atualizar um produto inválido")
    public void shouldDoesNotUpdatePriceNegative() throws Exception {
        String id = "1";
        ProductDTO product = 
                ProductDTO
                    .builder()
                    .name(getProductDTO().getName())
                    .description(getProductDTO().getDescription())
                    .price(-19.9)
                    .build();
        String json = new ObjectMapper().writeValueAsString(product);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .put(PRODUCT_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);
        
        mvc.perform(request)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("status_code").isNotEmpty())
                .andExpect(jsonPath("message").isNotEmpty());
    }
    
    @Test
    @DisplayName("Deve retornar um produto existente")
    public void getProdut() throws Exception {
        String id = "1";
        BDDMockito.given(service.getById(Mockito.any(String.class))).willReturn(Optional.of(getProduct()));
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(getProduct().getName()))
                .andExpect(jsonPath("description").value(getProduct().getDescription()))
                .andExpect(jsonPath("price").value(getProduct().getPrice()));
    }
    
    @Test
    @DisplayName("Não deve retornar nada quando não existir o produto")
    public void shouldDoesNotGetInexistentProduct() throws Exception {
        String id = "1";
        BDDMockito.given(service.getById(Mockito.any(String.class))).willReturn(Optional.empty());
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API.concat("/"+id))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
            .andExpect(status().isNotFound());
    }
    
    @Test
    @DisplayName("Deve listar os produtos")
    public void getAllProducts() throws Exception{
        List<Product> products = Arrays.asList(getProduct());
        BDDMockito.given(service.findAll()).willReturn(products);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(1)));
    }
    
    @Test
    @DisplayName("Deve retornar uma lista vazia de produtos")
    public void shoulReturnEmptyListOfProducts() throws Exception{
        List<Product> products = new ArrayList<>();
        BDDMockito.given(service.findAll()).willReturn(products);
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
    
    @Test
    @DisplayName("Deve realizar a busca de produtos")
    public void searchProducts() throws Exception {
        String search = "/search?min_price=10.5&max_price=50&q=superget";
        
        Product product = Product
                .builder()
                .id("123")
                .name("Product superget")
                .description("Teste de produto")
                .price(20)
                .build();
        
        BDDMockito.given(service.search(Mockito.any(SearchProduct.class))).willReturn(Arrays.asList(product));
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API.concat(search))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",Matchers.hasSize(1)));
    }
    
    @Test
    @DisplayName("Deve retornar vazio mesmo com parâmetro vazio")
    public void searchProductsWithEmptyParametes() throws Exception {
        String search = "/search?min_price=&max_price=&q=superget";
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API.concat(search))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",Matchers.hasSize(0)));
    }
    
    @Test
    @DisplayName("Deve retornar a consulta vazia")
    public void searchEmpty() throws Exception {
        String search = "/search";
        
        BDDMockito.given(
                service.search(Mockito.any(SearchProduct.class))
        ).willReturn(
                new ArrayList<>()
        );
        
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get(PRODUCT_API.concat(search))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        
        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",Matchers.hasSize(0)));
    }
    
    @Test
    @DisplayName("Deve deletar um produto")
    public void deleteProduct() throws Exception {
        String id = "1";
        BDDMockito.given(
                service.getById(Mockito.anyString())
        ).willReturn(
                Optional.of(getProduct())
        );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PRODUCT_API.concat("/"+id));
        
        mvc.perform(request)
                .andExpect(status().isOk());
    }
    
    @Test
    @DisplayName("Deve deletar um produto")
    public void shouldDoesNotDeleteProduct() throws Exception {
        String id = "1";
        BDDMockito.given(
                service.getById(Mockito.anyString())
        ).willReturn(
                Optional.empty()
        );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete(PRODUCT_API.concat("/"+id));
        
        mvc.perform(request)
                .andExpect(status().isNotFound());
    }
}
