package com.example.dto.user;

import com.example.util.annotation.AdultAge;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {

    @JsonView(Output.class)
    private Long id;

    @JsonView(InputSomeFields.class)
    @Email(message = "Invalid email format. Please provide a valid email address.")
    @NotBlank(message = "Invalid email. Empty email")
    @Schema(description = "User's email", example = "example@example.com")
    private String email;

    @JsonView(InputSomeFields.class)
    @NotBlank(message = "Invalid first name. Empty first name")
    @Size(min = 3, max = 30, message = "Invalid first name. Must be of 3 - 30 characters")
    @Schema(description = "User's first name", example = "John")
    private String firstName;

    @JsonView(InputSomeFields.class)
    @NotBlank(message = "Invalid last name. Empty last name")
    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @JsonView(InputSomeFields.class)
    @Past
    @AdultAge
    @Schema(description = "User's birth date", example = "1990-01-01")
    @NotNull(message = "Invalid birth date. Birth date is NULL")
    private Date birthDate;

    @JsonView(Input.class)
    @Schema(description = "User's address", example = "123 Street, City")
    private String address;

    @JsonView(Input.class)
    @Pattern(regexp = "\\d{10}")
    @Schema(description = "User's phone number", example = "1234567890")
    private String phoneNumber;


    public interface InputSomeFields {
    }

    public interface Input extends InputSomeFields {
    }

    public interface Output extends Input {
    }
}