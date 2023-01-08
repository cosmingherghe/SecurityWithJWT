package dev.cosmingherghe.security.auth;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import dev.cosmingherghe.security.config.JwtService;
import dev.cosmingherghe.security.dao.IUserRepository;
import dev.cosmingherghe.security.entity.Role;
import dev.cosmingherghe.security.entity.User;

public class AuthenticationService {

  private final IUserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;
  
  public AuthenticationResponse register(RegisterRequest request) {

    var user = User.builder()
            .username(request.getFullName())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
    userRepo.save(user);

    var jwToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
          .jwToken(jwToken)
          .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequst request) {
    
    authenticationManager.authenticate(
      new UsernamePasswordAuthenticationToken(
        request.getUsername(), 
        request.getPassword()
        )
    );

    var user = userRepo.findByUserName(request.getUsername())
          .orElseThrow();
    
    var jwToken = jwtService.generateToken(user);

    return AuthenticationResponse.builder()
                .jwToken(jwToken)
                .build();
  }

}
