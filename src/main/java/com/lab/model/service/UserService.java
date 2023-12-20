package com.lab.model.service;

import com.lab.model.config.util.Role;
import com.lab.model.model.RoleEntity;
import com.lab.model.model.UserEntity;
import com.lab.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
/* Will create the necessary constructor for the DI mechanism, but I do not recommend.
*  You should explicitly put the @Autowired annotation on the constructor. */
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserEntity> optUser = userRepository.findByEmail(email);
        if(optUser.isPresent()){
            UserEntity appUser = optUser.get();
            return new User(appUser.getEmail(), appUser.getPassword(), true, true, true, true,
                    /* User Roles */
                    Objects.isNull(appUser.getRoles()) ?
                            new ArrayList<>(List.of(new SimpleGrantedAuthority("INACTIVE")))
                            : appUser.getRoles()
                            .stream()
                            .map(RoleEntity::getName)
                            .map(SimpleGrantedAuthority::new)
                            .toList()
            );
        }
        throw new UsernameNotFoundException(email);
    }

    public void register(UserEntity user) {
        /* Encrypt password */
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        /* each user saved/registered receive the AUTH role */
        roleService.findByName(Role.AUTH).ifPresent(user::addRole);
        userRepository.save(user);
    }

    public void save(UserEntity user) {
        /* Encrypt password */
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userRepository.save(user);
    }
    public void login(UserEntity user, Authentication authentication) {
        UserDetails userDetails = this.loadUserByUsername(user.getEmail());
        if(Objects.isNull(userDetails))
            return;

        /* Here you set your user on the current session. You can also save the user entity directly. */
        authentication = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        // UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public Optional<UserEntity> findByEmail(String adminEmail) {
        return userRepository.findByEmail(adminEmail);
    }

    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    public UserEntity findById(Long userId) {
        return userRepository.findById(userId).get();
    }
}
