package com.ricardtadeiromero.ecommerce.order;


import com.ricardtadeiromero.ecommerce.customer.CustomerClient;
import com.ricardtadeiromero.ecommerce.exception.BusinessException;
import com.ricardtadeiromero.ecommerce.kafka.OrderConfirmation;
import com.ricardtadeiromero.ecommerce.kafka.OrderProducer;
import com.ricardtadeiromero.ecommerce.orderline.OrderLineRequest;
import com.ricardtadeiromero.ecommerce.orderline.OrderLineService;
import com.ricardtadeiromero.ecommerce.product.ProductClient;
import com.ricardtadeiromero.ecommerce.product.PurchaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final OrderRepository repository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;

    public Integer createOrder(OrderRequest request) {
        var customer = this.customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No customer id with the provided ID: " + request.customerId()));

        var purchasedProducts = this.productClient.purchaseProducts(request.products());

        var order = this.repository.save(mapper.toOrder(request));

        for(PurchaseRequest purchaseRequest: request.products()) {
            orderLineService.saveOrderLine(
                    new OrderLineRequest(
                            null,
                            order.getId(),
                            purchaseRequest.productId(),
                            purchaseRequest.quantity()
                    )
            );
        }

        //todo start payment process

        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchasedProducts
                )
        );

        return order.getId();
    }
}
