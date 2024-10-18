package com.ricardtadeiromero.ecommerce.kafka;

import com.ricardtadeiromero.ecommerce.customer.CustomerResponse;
import com.ricardtadeiromero.ecommerce.order.PaymentMethod;
import com.ricardtadeiromero.ecommerce.product.PurchaseResponse;

import java.math.BigDecimal;
import java.util.List;

public record OrderConfirmation(
        String orderReferences,
        BigDecimal totalAmount,
        PaymentMethod paymentMethod,
        CustomerResponse customer,
        List<PurchaseResponse> products
) {
}
