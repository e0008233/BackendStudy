package day.day.up.programming.payment;

import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import day.day.up.programming.payment.domain.OrderDetail;

public interface PayPayService {
    String authorizePayment(OrderDetail orderDetail)
            throws PayPalRESTException;

    Payment getPaymentDetails(String paymentId) throws PayPalRESTException;

    Payment executePayment(String paymentId, String payerId)
            throws PayPalRESTException;
}
