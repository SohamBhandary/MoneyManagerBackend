package com.Soham.MoneyManager.Service;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;

import java.io.OutputStream;
import java.util.List;

public interface ExcelService {
     void writeExpensesToExcel(OutputStream os, List<ExpenseDTO> exp);
     void writeIncomesToExcel(OutputStream os, List<IncomeDTO> incomes);
}
