package com.fs.main.service;

import com.fs.main.entity.Order;
import com.fs.main.entity.OrderItem;
import com.fs.main.entity.Product;
import com.fs.main.repository.OrderItemRepository;
import com.fs.main.repository.OrderRepository;
import com.fs.main.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional
    public Order placeOrder(Order order) {
        // Check stock and calculate total
        double totalAmount = 0;
        for (OrderItem item : order.getItems()) {
            Product product = productRepository.findById(item.getProduct().getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            // Stock check
            if (product.getStockQuantity() < item.getQuantity()) {
                throw new RuntimeException("Insufficient stock for " + product.getName());
            }

            item.setItemPrice(product.getPrice());
            totalAmount += product.getPrice() * item.getQuantity();
            // Decrement stock
            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
            productRepository.save(product);
        }

        order.setTotalAmount(totalAmount);
        return orderRepository.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getStatus() == Order.OrderStatus.COMPLETED) {
            throw new RuntimeException("Cannot cancel completed order");
        }

        // Restore stock
        for (OrderItem item : order.getItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setStatus(Order.OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    public List<Order> getOrdersByCustomerId(Long customerId) {
        return orderRepository.findByCustomerCustomerId(customerId);
    }
}
