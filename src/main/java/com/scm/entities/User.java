package com.scm.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User implements UserDetails {
  @Id
  private String userId;

  @Column(nullable = false)
  private String name;
  @Column(unique = true, nullable = false)
  private String email;

  @Getter(value = AccessLevel.NONE)
  private String password;
  @Column(length = 5000)
  private String about;
  @Column(length = 5000)
  private String profilePic;
  private String phoneNumebr;

  @Builder.Default
  private boolean enabled =false;
  @Builder.Default
  private boolean emailVerified = false;
  @Builder.Default
  private boolean phoneVerified = false;

  // Self google github
  @Enumerated(value = EnumType.STRING)
  @Builder.Default
  private Providers provider = Providers.Self;
  private String providerUserId;
  @Builder.Default
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
  private List<Contact> contacts = new ArrayList<>();

  @ElementCollection(fetch = FetchType.EAGER)
  @Builder.Default
  private List<String> roleList = new ArrayList<>();


  private String emailToken;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {

    Collection<SimpleGrantedAuthority> roles = roleList.stream().map(role -> new SimpleGrantedAuthority(role))
        .collect(Collectors.toList());

    return roles;
  }

  @Override
  public String getUsername() {
    return this.email;
  }

  @Override
  public String getPassword() {
    return this.password;
  }



}
