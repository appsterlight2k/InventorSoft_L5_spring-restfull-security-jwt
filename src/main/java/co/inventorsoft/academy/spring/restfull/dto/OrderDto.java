package co.inventorsoft.academy.spring.restfull.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long item_id;
    private Long customer_id;
    private String customer_firstname;
    private String customer_lastname;
    private String phone;
    private Boolean is_paid;
}
