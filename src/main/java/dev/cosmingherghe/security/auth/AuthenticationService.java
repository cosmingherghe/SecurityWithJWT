package dev.cosmingherghe.security.auth;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.cosmingherghe.security.config.JwtService;
import dev.cosmingherghe.security.dao.IUserRepository;
import dev.cosmingherghe.security.entity.Role;
import dev.cosmingherghe.security.entity.User;

public class AuthenticationService {

  private final IUserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  
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
    return null;
  }

}
