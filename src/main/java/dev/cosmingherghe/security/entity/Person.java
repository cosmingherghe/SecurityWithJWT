package dev.cosmingherghe.security.entity;

import dev.cosmingherghe.security.auth.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@NoArgsConstructor @AllArgsConstructor
@Table(name = "person")
@Entity
public class Person {
  @Id
  @GeneratedValue
  private Long id;
  private String fullName;
  private String email;
  private String telephone;

  @OneToOne(cascade = CascadeType.ALL, 
            mappedBy = "person", 
            fetch = FetchType.LAZY)
  private User user;
}
