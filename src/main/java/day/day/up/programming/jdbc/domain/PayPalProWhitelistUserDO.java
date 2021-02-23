package day.day.up.programming.jdbc.domain;

import java.time.LocalDateTime;

public class PayPalProWhitelistUserDO {

    private Integer userId;
    private Integer maxPriceLimit;
    private Integer maxCreditLimit;
    private Integer operatorId;
    private Integer logId;
    private Integer maxTopUpTimes;

    private LocalDateTime updateTime;

    public PayPalProWhitelistUserDO() {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMaxPriceLimit() {
        return maxPriceLimit;
    }

    public void setMaxPriceLimit(Integer maxPriceLimit) {
        this.maxPriceLimit = maxPriceLimit;
    }

    public Integer getMaxCreditLimit() {
        return maxCreditLimit;
    }

    public void setMaxCreditLimit(Integer maxCreditLimit) {
        this.maxCreditLimit = maxCreditLimit;
    }

    public Integer getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Integer operatorId) {
        this.operatorId = operatorId;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getMaxTopUpTimes() {
        return maxTopUpTimes;
    }

    public void setMaxTopUpTimes(Integer maxTopUpTimes) {
        this.maxTopUpTimes = maxTopUpTimes;
    }

}
