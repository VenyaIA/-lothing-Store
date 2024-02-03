package com.example.storeserver.dto;

import com.example.storeserver.entity.enums.EnumRole;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Data
public class CustomerDTO {

    private Long id;
    private String email;
    @NotEmpty
    private String firstname;
    @NotEmpty
    private String lastname;
    private String username;
    private String phoneNumber;
    private Set<EnumRole> roles = new HashSet<>();

}
