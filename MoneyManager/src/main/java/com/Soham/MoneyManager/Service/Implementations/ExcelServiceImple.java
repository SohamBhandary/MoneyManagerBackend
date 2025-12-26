package com.Soham.MoneyManager.Service.Implementations;

import com.Soham.MoneyManager.DTO.ExpenseDTO;
import com.Soham.MoneyManager.DTO.IncomeDTO;
import com.Soham.MoneyManager.Service.ExcelService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class ExcelServiceImple implements ExcelService {

    public void writeIncomesToExcel(OutputStream os, List<IncomeDTO> incomes){
        log.info("Writing {} incomes to Excel", incomes.size());

        try(Workbook workbook = new XSSFWorkbook()){
         Sheet sheet= workbook.createSheet("Incomes");
        Row header= sheet.createRow(0);
        header.createCell(0).setCellValue("S.No");
        header.createCell(1).setCellValue("Name");
        header.createCell(2).setCellValue("Category");
        header.createCell(3).setCellValue("Date");
            IntStream.range(0, incomes.size()).forEach(i->{
                IncomeDTO incomeDTO=incomes.get(i);
                Row row=sheet.createRow(i+1);
                row.createCell(0).setCellValue(i+1);
                row.createCell(1).setCellValue(incomeDTO.getName()!=null?incomeDTO.getName():"N/A");
                row.createCell(2).setCellValue(incomeDTO.getCategoryId()!=null?incomeDTO.getCategoryName():"N/A");
                row.createCell(3).setCellValue(incomeDTO.getAmount()!=null?incomeDTO.getAmount().doubleValue():0);
                row.createCell(4).setCellValue(incomeDTO.getDate()!=null?incomeDTO.getDate().toString():"N/A");

            });
            workbook.write(os);
            log.info("Successfully wrote {} incomes to Excel", incomes.size());




        } catch (IOException e) {
            log.error("Failed to write incomes to Excel: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }


    }
    public void writeExpensesToExcel(OutputStream os, List<ExpenseDTO> exp){

        try(Workbook workbook = new XSSFWorkbook()){
            log.info("Writing {} expenses to Excel", exp.size());
            Sheet sheet= workbook.createSheet("Expenses");
            Row header= sheet.createRow(0);
            header.createCell(0).setCellValue("S.No");
            header.createCell(1).setCellValue("Name");
            header.createCell(2).setCellValue("Category");
            header.createCell(3).setCellValue("Date");
            IntStream.range(0, exp.size()).forEach(i->{
                ExpenseDTO expenseDTO=exp.get(i);
                Row row=sheet.createRow(i+1);
                row.createCell(0).setCellValue(i+1);
                row.createCell(1).setCellValue(expenseDTO.getName()!=null?expenseDTO.getName():"N/A");
                row.createCell(2).setCellValue(expenseDTO.getCategoryId()!=null?expenseDTO.getCategoryName():"N/A");
                row.createCell(3).setCellValue(expenseDTO.getAmount()!=null?expenseDTO.getAmount().doubleValue():0);
                row.createCell(4).setCellValue(expenseDTO.getDate()!=null?expenseDTO.getDate().toString():"N/A");

            });
            workbook.write(os);
            log.info("Successfully wrote {} expenses to Excel", exp.size());




        } catch (IOException e) {
            log.error("Failed to write expenses to Excel: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }


    }

}
