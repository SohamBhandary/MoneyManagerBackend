package com.Soham.MoneyManager.Service;

import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    @Autowired
    private ProfileRepository profileRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
     Profile exsistingProfile= profileRepository.findByEmail(email).orElseThrow(()-> new UsernameNotFoundException("profile not found with email" +email));
     return User.builder().username(exsistingProfile.getEmail())
             .password(exsistingProfile.getPassword()).authorities(Collections.emptyList()).build();


    }
}
