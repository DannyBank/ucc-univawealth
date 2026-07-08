package com.dbank.uccunivawealth.model;

public class Investment {

    private Integer investmentId;
    private Integer userId;
    private String investmentName;
    private String investmentType;
    private Double principal;
    private Double interestRate;
    private Integer durationMonths;
    private String startDate;
    private String maturityDate;
    private Double expectedReturn;
    private String status;

    public Investment() {}

    public Investment(Integer investmentId,
                      Integer userId,
                      String investmentName,
                      String investmentType,
                      Double principal,
                      Double interestRate,
                      Integer durationMonths,
                      String startDate,
                      String maturityDate,
                      Double expectedReturn,
                      String status) {
        this.investmentId = investmentId;
        this.userId = userId;
        this.investmentName = investmentName;
        this.investmentType = investmentType;
        this.principal = principal;
        this.interestRate = interestRate;
        this.durationMonths = durationMonths;
        this.startDate = startDate;
        this.maturityDate = maturityDate;
        this.expectedReturn = expectedReturn;
        this.status = status;
    }

    public Integer getInvestmentId() {
        return investmentId;
    }

    public void setInvestmentId(Integer investmentId) {
        this.investmentId = investmentId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getInvestmentName() {
        return investmentName;
    }

    public void setInvestmentName(String investmentName) {
        this.investmentName = investmentName;
    }

    public String getInvestmentType() {
        return investmentType;
    }

    public void setInvestmentType(String investmentType) {
        this.investmentType = investmentType;
    }

    public Double getPrincipal() {
        return principal;
    }

    public void setPrincipal(Double principal) {
        this.principal = principal;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(Integer durationMonths) {
        this.durationMonths = durationMonths;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public Double getExpectedReturn() {
        return expectedReturn;
    }

    public void setExpectedReturn(Double expectedReturn) {
        this.expectedReturn = expectedReturn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Investment{" +
                "investmentId=" + investmentId +
                ", userId=" + userId +
                ", investmentName='" + investmentName + '\'' +
                ", investmentType='" + investmentType + '\'' +
                ", principal=" + principal +
                ", interestRate=" + interestRate +
                ", durationMonths=" + durationMonths +
                ", startDate='" + startDate + '\'' +
                ", maturityDate='" + maturityDate + '\'' +
                ", expectedReturn=" + expectedReturn +
                ", status='" + status + '\'' +
                '}';
    }

    public Double getBalance() {
        if (principal == null || interestRate == null || durationMonths == null) {
            return 0.0;
        }
        double rate = interestRate / 100.0;
        return principal + (principal * rate * durationMonths / 12.0);
    }
}
