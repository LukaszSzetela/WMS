package pl.szetela.lukasz.WMS.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ProductLogDto {
    private Long id;
    private String name;
    private Double price;
    private Integer number;
    private Date date;
    private String category;
    private String subcategory;
    private Long productId;
}
