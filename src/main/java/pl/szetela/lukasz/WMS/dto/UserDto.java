package pl.szetela.lukasz.WMS.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private Long userId;

    private String firstName;

    private String lastName;

    private String email;

    private String username;

    private String password;

    private Boolean active;

    private String role;

    private Long customerId;

}
