package day.day.up.programming.payment.domain;

import com.paypal.api.payments.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResp implements Serializable {
    private boolean isSuccess;
    private String redirectUrl;
    private Transaction transaction;

    public PaymentResp(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

    public PaymentResp(boolean isSuccess,String redirectUrl){
        this.isSuccess = isSuccess;
        this.redirectUrl = redirectUrl;
    }

    public PaymentResp(boolean isSuccess,Transaction transaction){
        this.isSuccess = isSuccess;
        this.transaction = transaction;
    }
}
