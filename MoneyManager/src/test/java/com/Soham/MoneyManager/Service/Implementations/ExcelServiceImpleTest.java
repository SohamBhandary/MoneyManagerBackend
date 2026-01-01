package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelServiceImpleTest {

    private ExcelServiceImple excelService;

    @BeforeEach
    void setUp() {
        excelService = new ExcelServiceImple();
    }

    @Test
    void writeIncomesToExcel_success() throws Exception {
        IncomeDTO income = IncomeDTO.builder()
                .name("Salary")
                .categoryId(1L)               // ✅ FIX
                .categoryName("Job")
                .amount(new BigDecimal("50000"))
                .date(LocalDate.now())
                .build();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        excelService.writeIncomesToExcel(os, List.of(income));

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(os.toByteArray()));
        Sheet sheet = workbook.getSheet("Incomes");

        Row row = sheet.getRow(1);

        assertEquals("Salary", row.getCell(1).getStringCellValue());
        assertEquals("Job", row.getCell(2).getStringCellValue());      // ✅ PASS
        assertEquals(50000, row.getCell(3).getNumericCellValue());

        workbook.close();
    }

    @Test
    void writeExpensesToExcel_success() throws Exception {
        ExpenseDTO expense = ExpenseDTO.builder()
                .name("Food")
                .categoryId(2L)               // ✅ FIX
                .categoryName("Groceries")
                .amount(new BigDecimal("1000"))
                .date(LocalDate.now())
                .build();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        excelService.writeExpensesToExcel(os, List.of(expense));

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(os.toByteArray()));
        Sheet sheet = workbook.getSheet("Expenses");

        Row row = sheet.getRow(1);

        assertEquals("Food", row.getCell(1).getStringCellValue());
        assertEquals("Groceries", row.getCell(2).getStringCellValue()); // ✅ PASS
        assertEquals(1000, row.getCell(3).getNumericCellValue());

        workbook.close();
    }

    @Test
    void writeIncomesToExcel_handlesNullValues() throws Exception {
        IncomeDTO income = IncomeDTO.builder().build();

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        excelService.writeIncomesToExcel(os, List.of(income));

        Workbook workbook = new XSSFWorkbook(new ByteArrayInputStream(os.toByteArray()));
        Sheet sheet = workbook.getSheet("Incomes");

        Row row = sheet.getRow(1);

        assertEquals("N/A", row.getCell(1).getStringCellValue());
        assertEquals("N/A", row.getCell(2).getStringCellValue());

        workbook.close();
    }
}
