package com.Soham.MoneyManager.Repositories;

import com.Soham.MoneyManager.Entities.Expense;
import com.Soham.MoneyManager.Entities.Income;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface IncomeReposiotry extends JpaRepository<Income,Long> {
    List<Income> findByProfileIdOrderByDateDesc(Long profileId);
    List<Income> findTop5ByProfileIdOrderByDateDesc(Long profileId);
    @Query("SELECT SUM(e." +
            "amount) FROM Income e where e.profile.id=:profileId")
    BigDecimal findTotalExpenseByProfileId(@Param("profileId") Long profileId);

    List<Income>  findByProfileIdAndDateBetweenAndNameContainingIgnoreCase(Long profileId, LocalDate startDate, LocalDate endDate, String keyword, Sort sort);
    List<Income> findByProfileIdAndDateBetween(Long profileId, LocalDate startDate, LocalDate endDate);
}
