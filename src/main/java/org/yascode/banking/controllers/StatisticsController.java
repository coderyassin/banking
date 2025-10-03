package org.yascode.banking.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yascode.banking.dto.TransactionSumDetails;
import org.yascode.banking.services.StatisticsService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/statistics")
public class StatisticsController {

  private final StatisticsService service;

    public StatisticsController(StatisticsService service) {
        this.service = service;
    }

    @GetMapping("/sum-by-date/{user-id}")
  public ResponseEntity<List<TransactionSumDetails>> findSumTractionsByDate(
      @PathVariable("user-id") Integer userId,
      @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
      @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate
  ){
    return ResponseEntity.ok(service.findSumTractionsByDate(startDate, endDate, userId));
  }

  @GetMapping("/account-balance/{user-id}")
  public ResponseEntity<BigDecimal> getAccountBalance(
      @PathVariable("user-id") Integer userId
  ){
    return ResponseEntity.ok(service.getAccountBalance(userId));
  }

  @GetMapping("/highest-transfer/{user-id}")
  public ResponseEntity<BigDecimal> highestTransfer(
      @PathVariable("user-id") Integer userId
  ){
    return ResponseEntity.ok(service.highestTransfer(userId));
  }

  @GetMapping("/highest-deposit/{user-id}")
  public ResponseEntity<BigDecimal> highestDeposit(
      @PathVariable("user-id") Integer userId
  ){
    return ResponseEntity.ok(service.highestDeposit(userId));
  }
}
