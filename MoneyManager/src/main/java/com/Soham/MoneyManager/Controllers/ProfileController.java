package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor

public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(ProfileDTO profileDTO){
        ProfileDTO registeredUser=profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }
}
