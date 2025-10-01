package org.yascode.banking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.yascode.banking.models.User;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LightUserDto {

  private Integer id;

  @NotNull(message = "Le prenom ne doit pas etre vide")
  @NotEmpty(message = "Le prenom ne doit pas etre vide")
  @NotBlank(message = "Le prenom ne doit pas etre vide")
  private String firstName;

  @NotNull(message = "Le nom ne doit pas etre vide")
  @NotEmpty(message = "Le nom ne doit pas etre vide")
  @NotBlank(message = "Le nom ne doit pas etre vide")
  private String lastName;

  public static LightUserDto fromEntity(User user) {
    // null check
    return LightUserDto.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .build();
  }

  public static User toEntity(LightUserDto user) {
    // null check
    return User.builder()
        .id(user.getId())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .build();
  }

}
