package com.ecommerce.book_store.schedule;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.persistent.entity.Order;
import com.ecommerce.book_store.service.abstraction.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
public class OrderExpiredDisablingSchedule {
    private final OrderService orderService;

    public OrderExpiredDisablingSchedule(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(fixedRate = 1800000)
    public void disableOrdersExactlyAfter1Day() {
        try {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime maxTime = now.minusDays(1);
            LocalDateTime minTime = maxTime.minusMinutes(30);

            List<Order> orders = orderService.findOrdersCreatedExactly24HoursAgo(
                    OrderStatus.PENDING,
                    minTime,
                    maxTime
            );

            for (Order order : orders) {
                order.setStatus(OrderStatus.CANCELLED);
            }

            orderService.getRepository().saveAll(orders);

            if (!orders.isEmpty()) {
                log.info("[OrderExpiredDisablingSchedule] Disabled {} orders", orders.size());
            } else {
                log.info("[OrderExpiredDisablingSchedule] No orders to disable");
            }
        } catch (Exception e) {
            log.error("[OrderExpiredDisablingSchedule] Error: {}", e.getMessage());
        }
    }


}
