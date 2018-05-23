package pl.szetela.lukasz.WMS.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {

    private Long productId;
    private String category;
    private String subcategory;
    private String name;
    private Integer number;
    private Double price;
    private Integer ZB;
    private Integer ZI;
    private Integer demand;
    private Integer reservedNumber;
}
