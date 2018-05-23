package pl.szetela.lukasz.WMS.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.szetela.lukasz.WMS.enums.Status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private Long ordererId;
    private Double shippingCost;
    private Double tax;
    private Date orderDate;
    private Double taxRate;
    private Double subTotal;
    private Double totalCost;
    private String status;
    private Long executorId;
    private List<ResponseProduct> products = new ArrayList<>();
    private Boolean verification;
    private UserDto executor;
    private CustomerDto orderer;
    private String pickingTime;
}
