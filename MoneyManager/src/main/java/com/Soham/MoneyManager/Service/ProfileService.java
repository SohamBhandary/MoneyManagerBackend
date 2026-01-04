package com.Soham.MoneyManager.Service;


import com.Soham.MoneyManager.DTO.AuthDTO;
import com.Soham.MoneyManager.DTO.ProfileDTO;
import com.Soham.MoneyManager.Entities.Profile;

import java.util.Map;

public interface ProfileService {

    ProfileDTO registerProfile(ProfileDTO profileDTO);

    boolean isAccountActive(String email);
    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO);
    public Profile toEntity(ProfileDTO  profileDTO);
    public ProfileDTO toDTO(Profile profile);
    public Profile getCurrentProfile();



}
