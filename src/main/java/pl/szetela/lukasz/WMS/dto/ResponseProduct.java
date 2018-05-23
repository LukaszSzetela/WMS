package pl.szetela.lukasz.WMS.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseProduct {

    private Long productId;
    private Integer number;
    private String name;
    private Double price;
    private Integer inStock;
    private Boolean isComplete = false;
    private Integer ordinal;
}
