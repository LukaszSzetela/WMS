package pl.szetela.lukasz.WMS.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerDto {
    private Long customerId;
    private String email;
    private String name;
    private Double deliveryCost;
    private Double vatRate;
    private String nip;
    private String regon;
    private Long userId;
}
