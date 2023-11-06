package com.greenblat.cloudfilestorage.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.greenblat.cloudfilestorage.validation.OnCreate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRequest(

        @NotBlank
        String username,

        @JsonProperty(access = Access.WRITE_ONLY)
        @NotBlank
        @Size(min = 8)
        String password,

        @NotBlank(groups = OnCreate.class)
        @Email(groups = OnCreate.class)
        String email,

        @Pattern(
                regexp = "^(\\+\\d{1,3}( )?)?((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$",
                groups = OnCreate.class
        )
        @NotBlank(groups = OnCreate.class)
        String phoneNumber
) {
}
