package com.Soham.MoneyManager.Controllers;

import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor

public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileDTO> registerProfile(@RequestBody ProfileDTO profileDTO){
        ProfileDTO registeredUser=profileService.registerProfile(profileDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }


    @GetMapping("/activate")
    public ResponseEntity<String> activate(@RequestParam String token){
        boolean isActivated=profileService.activateProfile(token);
        if(isActivated){
            return  ResponseEntity.ok("profile activated succesfully");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Activation Token AlreadyUSed");
        }

    }
}
