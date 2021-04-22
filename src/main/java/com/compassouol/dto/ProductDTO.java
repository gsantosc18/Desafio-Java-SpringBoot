package com.compassouol.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor @AllArgsConstructor
public class ProductDTO {
    private String id;
    @NotEmpty(message = "Inválido nome vazio. ")
    private String name;
    @NotEmpty(message = "Inválido descrição vazia. ")
    private String description;
    @Min(value = 0, message = "O valor do produto não deve menor que ZERO. ")
    private double price;
}
