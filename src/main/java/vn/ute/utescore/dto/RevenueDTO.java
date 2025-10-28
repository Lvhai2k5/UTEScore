package vn.ute.utescore.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RevenueDTO {
    private LocalDate date;
    private BigDecimal totalRevenue;
    private Long totalTransactions;
    private BigDecimal averageRevenue;
    private BigDecimal previousPeriodRevenue;
    private BigDecimal changePercentage;
    
    public RevenueDTO() {}
    
    public RevenueDTO(LocalDate date, BigDecimal totalRevenue, Long totalTransactions) {
        this.date = date;
        this.totalRevenue = totalRevenue;
        this.totalTransactions = totalTransactions;
        if (totalTransactions != null && totalTransactions > 0) {
            this.averageRevenue = totalRevenue.divide(BigDecimal.valueOf(totalTransactions), java.math.RoundingMode.HALF_UP);
        } else {
            this.averageRevenue = BigDecimal.ZERO;
        }
    }
    
    // Getters and Setters
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    
    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
    
    public Long getTotalTransactions() { return totalTransactions; }
    public void setTotalTransactions(Long totalTransactions) { this.totalTransactions = totalTransactions; }
    
    public BigDecimal getAverageRevenue() { return averageRevenue; }
    public void setAverageRevenue(BigDecimal averageRevenue) { this.averageRevenue = averageRevenue; }
    
    public BigDecimal getPreviousPeriodRevenue() { return previousPeriodRevenue; }
    public void setPreviousPeriodRevenue(BigDecimal previousPeriodRevenue) { this.previousPeriodRevenue = previousPeriodRevenue; }
    
    public BigDecimal getChangePercentage() { return changePercentage; }
    public void setChangePercentage(BigDecimal changePercentage) { this.changePercentage = changePercentage; }
    
    // Helper methods
    public boolean isPositiveChange() {
        return changePercentage != null && changePercentage.compareTo(BigDecimal.ZERO) > 0;
    }
    
    public String getChangePercentageFormatted() {
        if (changePercentage == null) return "0%";
        return String.format("%.2f%%", changePercentage);
    }
}

