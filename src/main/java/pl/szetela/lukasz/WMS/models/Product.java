package pl.szetela.lukasz.WMS.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "products")
@NoArgsConstructor
@Getter
@Setter
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;
    private String category;
    private String subcategory;
    private String name;
    private Double price;
    private Integer number;
    private Integer reservedNumber;
    private Double shortageCost;
    private Double annualCostStock;
    private Integer deliveryTime;
}