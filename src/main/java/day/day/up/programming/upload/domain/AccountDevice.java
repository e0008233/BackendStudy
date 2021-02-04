package day.day.up.programming.upload.domain;

import java.sql.Timestamp;

/* Mapping to DB table: rings_account.account_device */
public class AccountDevice {
    private String pi;
    private int num;
    private Timestamp createTime;
    private Timestamp updateTime;

    public AccountDevice() {
    }

    public AccountDevice(String pi, int num, Timestamp createTime, Timestamp updateTime) {
        this.pi = pi;
        this.num = num;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public void setPi(String pi) {
        this.pi = pi;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public String getPi() {
        return pi;
    }

    public int getNum() {
        return num;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }
}
