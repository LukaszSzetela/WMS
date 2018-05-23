package pl.szetela.lukasz.WMS.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionUser {
    private Integer userId;
    private String role;
    private Double vatRate;
    private Double deliveryCost;

    public SessionUser(Integer userId, String role) {
        this.userId = userId;
        this.role = role;
    }
}
