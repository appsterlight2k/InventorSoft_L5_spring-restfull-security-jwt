package co.inventorsoft.academy.spring.restfull.service;

import co.inventorsoft.academy.spring.restfull.dao.UserRepository;
import co.inventorsoft.academy.spring.restfull.exception.UserAlreadyExistsException;
import co.inventorsoft.academy.spring.restfull.model.Role;
import co.inventorsoft.academy.spring.restfull.model.User;
import co.inventorsoft.academy.spring.restfull.dto.UserDto;
import co.inventorsoft.academy.spring.restfull.util.MapperUtil;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;
    @Autowired
    MapperUtil mapperUtil;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Autowired
    public JwtUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("User %s wasn't found: ", username )));

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().toString());
        List<GrantedAuthority> authorities = Collections.singletonList(authority);


        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    public User save(UserDto userDto) {

        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new UserAlreadyExistsException(String.format("A user with the name %s already exists!", userDto.getUsername()));
        }

        User user = mapperUtil.convertToEntity(userDto, User.class);
        user.setRole(Role.ROLE_USER);
        user.setPassword(bcryptEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }
}
