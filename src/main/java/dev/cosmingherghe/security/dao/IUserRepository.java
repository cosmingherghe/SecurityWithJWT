package dev.cosmingherghe.security.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.cosmingherghe.security.entity.User;

public interface IUserRepository extends JpaRepository<User, Long> {

  Optional<User> findByUserName(String username);
}
