package co.inventorsoft.academy.spring.restfull.dto;

import co.inventorsoft.academy.spring.restfull.model.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String password;
    private Role role;

}
