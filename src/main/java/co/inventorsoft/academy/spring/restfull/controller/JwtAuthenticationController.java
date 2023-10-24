package co.inventorsoft.academy.spring.restfull.controller;

import co.inventorsoft.academy.spring.restfull.dto.UserDto;
import co.inventorsoft.academy.spring.restfull.model.User;
import co.inventorsoft.academy.spring.restfull.model.jwt.JwtRequest;
import co.inventorsoft.academy.spring.restfull.model.jwt.JwtResponse;
import co.inventorsoft.academy.spring.restfull.service.JwtUserDetailsService;
import co.inventorsoft.academy.spring.restfull.service.UserService;
import co.inventorsoft.academy.spring.restfull.util.JwtTokenUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@CrossOrigin
public class JwtAuthenticationController {
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private JwtUserDetailsService userDetailsService;
    private UserService userService;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, JwtUserDetailsService userDetailsService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsService;
        this.userService = userService;
    }

    //http://localhost:8080/sign-in
    @PostMapping("/sign-in")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);
        String successMessage = "You were authenticated successfully!";

        return ResponseEntity.ok(new JwtResponse(token, successMessage));
    }


    @PostMapping("/sign-up")
    public ResponseEntity<?> saveUser(@RequestBody UserDto userDto) throws Exception {
        return ResponseEntity.ok(userDetailsService.save(userDto));
    }


    @GetMapping("/all-users")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }


}
