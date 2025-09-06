package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.ProfileRepository;
import com.Soham.MoneyManager.Service.EmailService;
import com.Soham.MoneyManager.Service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImple implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private com.Soham.MoneyManager.Service.EmailService emailService;
    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {

     Profile newProfile= toEntity(profileDTO);
     newProfile.setActivationToken(UUID.randomUUID().toString());
     newProfile=profileRepository.save(newProfile);
        String activationLink = "http://localhost:8080/api/activate?token=" + newProfile.getActivationToken();
        String sub = "Activate Your Money Manager Account";
        String body = "Click on the following link to activate your account: " + activationLink;
     emailService.sendEmail(newProfile.getEmail(),sub,body);

     return toDTO(newProfile);



    }

    @Override
    public boolean activateProfile(String activationToken) {
        return profileRepository.findByActivationToken(activationToken).map(profile -> {profile.setIsActive(true);
        profileRepository.save(profile);
        return  true;} ).orElse(false);
    }

    public Profile toEntity(ProfileDTO  profileDTO){
        return Profile.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(profileDTO.getPassword())
                .profileImageUrl(profileDTO.getProfileImageUrl())
                .createdAt(profileDTO.getCreatedAt())
                .updatedAt(profileDTO.getUpdatedAt())
                .build();
    }
    public ProfileDTO toDTO(Profile profile){
        return ProfileDTO.builder()
                .id(profile.getId())
                .fullName(profile.getFullName())
                .email(profile.getEmail())
                .profileImageUrl(profile.getProfileImageUrl())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
