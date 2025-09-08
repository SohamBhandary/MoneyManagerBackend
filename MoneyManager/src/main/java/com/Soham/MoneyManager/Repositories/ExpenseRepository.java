package com.Soham.MoneyManager.Repositories;

import com.Soham.MoneyManager.Entities.Expense;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense,Long> {
   List<Expense> findByProfileIdOrderByDateDesc(Long profileId);
  List<Expense> findTop5ByProfileIdOrderByDateDesc(Long profileId);
  @Query("SELECT SUM(e.amount) FROM Expense e where e.profile.id=:profileId")
 BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<Expense> findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(
            Long profileId, LocalDate startDate, LocalDate endDate, String name, Sort sort);
List<Expense> findByProfileIdAndDateBetween(Long profileId,LocalDate startDate,LocalDate endDate);

List<Expense> findByProfileIdAndDate(Long profileId,LocalDate date);


}
