package com.spring.springoauth2.domain.user.dao;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    @Column(name = "user_picture")
    private String userPicture;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}
