package com.compassouol.controller;

import com.compassouol.dto.ProductDTO;
import com.compassouol.entity.Product;
import com.compassouol.exception.ProductInexistentException;
import com.compassouol.model.ApiError;
import com.compassouol.model.SearchProduct;
import com.compassouol.service.ProductService;
import java.util.List;
import javax.validation.Valid;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/products")
public class ProductController {    
    @Autowired @Qualifier("productimp")
    ProductService productService;
    
    ModelMapper modelMapper;

    public ProductController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDTO salve(@RequestBody @Valid ProductDTO productDTO) {
        Product product = this.modelMapper.map(productDTO, Product.class);
        Product entity = productService.save(product).get();
        return this.modelMapper.map(entity, ProductDTO.class);
    }
    
    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO update(@PathVariable String id, @RequestBody @Valid ProductDTO productDTO) {
        return productService.getById(id)
                .map( product -> {
                    product.setName(productDTO.getName());
                    product.setDescription(productDTO.getDescription());
                    product.setPrice(productDTO.getPrice());
                    product = productService.update(product);
                    return modelMapper.map(product, ProductDTO.class);
                })
                .orElseThrow(() -> new ProductInexistentException("Não foi possível encontrar o produto."));
    }
    
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductDTO getProduct(@PathVariable String id) {
        return productService.getById(id)
                .map( product ->  modelMapper.map(product, ProductDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
    }
    
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> getAll() {
        return modelMapper.map(productService.findAll(),new TypeToken<List<ProductDTO>>(){}.getType());
    }
    
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductDTO> search(@RequestParam(required = false, defaultValue = "") String q, @RequestParam(required = false, defaultValue = "") String min_price, @RequestParam(required = false, defaultValue = "") String max_price) {
        SearchProduct searchProduct = new SearchProduct();
        if(!q.trim().isEmpty()) {
            searchProduct.setQ(q);
        } 
        if (!min_price.trim().isEmpty()) {
            searchProduct.setMin_price(toDouble(min_price));
        }
        if (!max_price.trim().isEmpty()) {
            searchProduct.setMax_price(toDouble(max_price));
        }
        return modelMapper.map(productService.search(searchProduct),new TypeToken<List<ProductDTO>>(){}.getType());
    }
    
    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {
        Product product = productService.getById(id)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND) );
        productService.delete(product);
    }
        
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        return new ResponseEntity<>(new ApiError(bindingResult), HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(ProductInexistentException.class)
    public ResponseEntity<ApiError> handleProductInexistentExceptions(ProductInexistentException ex) {
        return new ResponseEntity<>(new ApiError(ex), HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiError> handleBindException(BindException ex) {
        return new ResponseEntity<>(new ApiError(ex), HttpStatus.BAD_REQUEST);
    }
    
    private double toDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}
