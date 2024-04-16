package com.group5.bookshelfregistry.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.group5.bookshelfregistry.annotations.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(unique = true,nullable = false)
	@NotBlank(message = "Username cannot be blank")
	private String username;

	@Column(nullable = false)
	@NotBlank(message = "Password cannot be blank")
	@JsonIgnore
	private String password;

	@Column(nullable = false)
	@Role
	private String role;



}