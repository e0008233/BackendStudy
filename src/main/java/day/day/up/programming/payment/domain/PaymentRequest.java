package day.day.up.programming.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest implements Serializable {
    private String product;
    private String subtotal;
    private String shipping;
    private String tax;
    private String total;
}
