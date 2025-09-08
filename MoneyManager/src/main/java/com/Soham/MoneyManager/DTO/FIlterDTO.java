package com.Soham.MoneyManager.DTO;

import lombok.Data;

import java.time.LocalDate;

@Data

public class FIlterDTO {

    private String type;
    private LocalDate start;
    private LocalDate end;
    private String keyword;
    private String sortField;
    private String sortOrder;


}
