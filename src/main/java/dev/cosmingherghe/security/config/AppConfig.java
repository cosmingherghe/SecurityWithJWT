package dev.cosmingherghe.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

  @Bean
  public AuthenticationProvider authenticationProvider() {
    
    DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
    daoAuthProvider.setUserDetailsService(userDetailsService());
    daoAuthProvider.setPasswordEncoder(passwordEncoder());

    return daoAuthProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
    return authConf.getAuthenticationManager();
  }
}
