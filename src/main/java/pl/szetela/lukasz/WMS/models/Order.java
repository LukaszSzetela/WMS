package pl.szetela.lukasz.WMS.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders")
@NoArgsConstructor
@Getter
@Setter
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orderer", referencedColumnName = "user_id")
    private User orderer;

    private Double shippingCost;

    private Double tax;

    private Timestamp orderDate;

    private Double taxRate;

    private Double subTotal;

    private Double totalCost;

    private String status;

    private String pickingTime;

    private Long currentRealizeTimestamp;

    @ManyToOne
    @JoinColumn(name = "executor", referencedColumnName = "user_id")
    private User executor;

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @JoinTable(name = "order_product",
            joinColumns = {@JoinColumn(name="order_id", referencedColumnName="order_id")},
            inverseJoinColumns = {@JoinColumn(name="product_id", referencedColumnName="product_id"), @JoinColumn(name = "number", referencedColumnName = "number")})
    private List<Product> products;
}
