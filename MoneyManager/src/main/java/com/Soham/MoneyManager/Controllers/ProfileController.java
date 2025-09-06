package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.AuthDTO;
import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    // Register new user
    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO registeredUser = profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String token) {
        boolean isActivated = profileService.activateProfile(token);
        if (isActivated) {
            return ResponseEntity.ok("Profile activated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Activation token already used or invalid");
        }
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
        try {

            if (!profileService.isAccountActive(authDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("message", "Account not active. Please activate your account first"));
            }

            Map<String, Object> res = profileService.authenticateAndGenerateToken(authDTO);
            return ResponseEntity.ok(res);

        }  catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Something went wrong: " + e.getMessage()));
        }
    }

    @GetMapping("/test")
    public String test(){
        return "test succesful";
    }
}
