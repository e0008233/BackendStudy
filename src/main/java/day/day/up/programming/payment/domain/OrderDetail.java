package day.day.up.programming.payment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private String productName;
    private String subtotal;
    private String shipping;
    private String tax;
    private String total;
}
