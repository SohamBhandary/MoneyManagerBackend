package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.AuthDTO;
import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Entities.Profile;
import com.Soham.MoneyManager.Repositories.ProfileRepository;
import com.Soham.MoneyManager.Service.EmailService;
import com.Soham.MoneyManager.Service.ProfileService;
import com.Soham.MoneyManager.Utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileServiceImple implements ProfileService {
    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private com.Soham.MoneyManager.Service.EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private final JWTUtil jwtUtil;

    @Value("${app.activation.url}")
    private String activationurl;

    @Override
    public ProfileDTO registerProfile(ProfileDTO profileDTO) {
        log.info("Registering new profile with email: {}", profileDTO.getEmail());


        Profile newProfile= toEntity(profileDTO);
     newProfile.setActivationToken(UUID.randomUUID().toString());

     newProfile=profileRepository.save(newProfile);
        log.info("Profile saved successfully with ID: {}", newProfile.getId());
        String activationLink = activationurl+"/activate?token=" + newProfile.getActivationToken();
        String sub = "Activate Your Money Manager Account";
        String body = "Click on the following link to activate your account: " + activationLink;
     emailService.sendEmail(newProfile.getEmail(),sub,body);
        log.info("Activation email sent to: {}", newProfile.getEmail());



     return toDTO(newProfile);



    }

    @Override
    public boolean activateProfile(String activationToken) {
        log.info("Activating profile with token: {}", activationToken);
        return profileRepository.findByActivationToken(activationToken).map(profile -> {profile.setIsActive(true);
        profileRepository.save(profile);
        return  true;} ).orElse(false);
    }

    public Profile toEntity(ProfileDTO  profileDTO){
        return Profile.builder()
                .id(profileDTO.getId())
                .fullName(profileDTO.getFullName())
                .email(profileDTO.getEmail())
                .password(passwordEncoder.encode(profileDTO.getPassword()))
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

    public boolean isAccountActive(String email){
        return profileRepository.findByEmail(email).map(Profile::getIsActive).orElse(false);

    }
    public Profile getCurrentProfile(){

       Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
      return profileRepository.
              findByEmail(authentication.getName()).
              orElseThrow(()->new UsernameNotFoundException("Profile not found with email address" + authentication.getName()));


    }

    public ProfileDTO getPublicProfile(String email){
        Profile currentUser=null;
        if(email==null){
           currentUser= getCurrentProfile();
        }
        else{
          currentUser=  profileRepository.findByEmail(email)
                  .orElseThrow(()->new UsernameNotFoundException("Profile not found with email address"+ email));

        }
        return ProfileDTO.builder()
                .id(currentUser.getId())
                .fullName(currentUser.getFullName())
                .profileImageUrl(currentUser.getProfileImageUrl())
                .createdAt(currentUser.getCreatedAt())
                .updatedAt(currentUser.getUpdatedAt())
                .build();

    }
    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));

            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of(
                    "token", token,
                    "user", getPublicProfile(authDTO.getEmail())
            );
        } catch (Exception e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

}
