package pl.szetela.lukasz.WMS.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "products_log")
@NoArgsConstructor
@Getter
@Setter
public class ProductLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_log_id")
    private Long id;

    private String name;

    private Double price;

    private Double shortageCost;

    private Double annualCostStock;

    private Integer deliveryTime;

    private Integer number;

    private Date date;

    private String category;

    private String subcategory;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
