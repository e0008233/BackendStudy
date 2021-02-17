package day.day.up.programming.web.controller;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.ShippingAddress;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.PayPalRESTException;
import day.day.up.programming.payment.PayPayService;
import day.day.up.programming.payment.domain.OrderDetail;
import day.day.up.programming.payment.domain.PaymentRequest;
import day.day.up.programming.payment.domain.PaymentResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PayPayService payPayService;

    private static String errorLink = "";
    @RequestMapping(value="/create_payment", method = RequestMethod.POST)
    public PaymentResp createPayment(PaymentRequest request){
        OrderDetail orderDetail = new OrderDetail(request.getProduct(), request.getSubtotal(),
                request.getShipping(), request.getTax(), request.getTotal());

        String approvalLink = null;
        try {
            approvalLink = payPayService.authorizePayment(orderDetail);
        } catch (PayPalRESTException e) {
            return new PaymentResp(false);
        }

        return new PaymentResp(true,approvalLink);
    }

    @RequestMapping(value="/review_payment", method = RequestMethod.GET)
    public PaymentResp reviewPayment(@RequestParam String paymentId, @RequestParam String PayerID){
        Payment payment = null;
        try {
            payment = payPayService.getPaymentDetails(paymentId);
        } catch (PayPalRESTException e) {
            return new PaymentResp(false);

        }

        PayerInfo payerInfo = payment.getPayer().getPayerInfo();
        Transaction transaction = payment.getTransactions().get(0);
        ShippingAddress shippingAddress = transaction.getItemList().getShippingAddress();

        String url = "review.jsp?paymentId=" + paymentId + "&PayerID=" + PayerID;

        return new PaymentResp(true,url);

    }

    @RequestMapping(value="/execute_payment", method = RequestMethod.GET)
    public PaymentResp executePayment(@RequestParam String paymentId, @RequestParam String PayerID){
        Payment payment = null;
        try {
            payment = payPayService.executePayment(paymentId, PayerID);

        } catch (PayPalRESTException ex) {
            return new PaymentResp(false);

        }

        return new PaymentResp(true);

    }

}
