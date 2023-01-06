package dev.cosmingherghe.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import dev.cosmingherghe.security.dao.IUserRepository;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

  private final IUserRepository userRepo;

  @Bean
  public UserDetailsService userDetailsService() {

    return username -> userRepo.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("UserName not found!"));
  }

}
