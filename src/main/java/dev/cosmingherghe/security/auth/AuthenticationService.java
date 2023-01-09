package dev.cosmingherghe.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.cosmingherghe.security.config.JwtService;
import dev.cosmingherghe.security.dao.IUserRepository;
import dev.cosmingherghe.security.entity.Role;
import dev.cosmingherghe.security.entity.User;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final IUserRepository userRepo;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {

    var username = User.builder()
        .username(request.getUsername())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();
        userRepo.save(username);

    var jwToken = jwtService.generateToken(username);

    return AuthenticationResponse.builder()
        .jwToken(jwToken)
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {

    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
        )
    );

    var username = userRepo.findByUsername(request.getUsername())
        .orElseThrow();
    var jwToken = jwtService.generateToken(username);

    return AuthenticationResponse.builder()
        .jwToken(jwToken)
        .build();
  }
}
