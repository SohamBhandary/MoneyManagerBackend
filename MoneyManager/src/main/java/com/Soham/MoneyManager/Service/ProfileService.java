package com.Soham.MoneyManager.Service;


import com.Soham.MoneyManager.DTO.ProfileDTO;

public interface ProfileService {

    ProfileDTO registerProfile(ProfileDTO profileDTO);
    boolean activateProfile(String activationToken);




}
