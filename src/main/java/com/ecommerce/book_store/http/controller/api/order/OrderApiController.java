package com.ecommerce.book_store.http.controller.api.order;

import com.ecommerce.book_store.core.constant.PaymentMethod;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.service.abstraction.OrderService;
import com.ecommerce.book_store.service.abstraction.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/v1/auth/orders")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderApiController(OrderService orderService, @Qualifier("payOSPaymentService") PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping("/order")
    public ResponseEntity<ApiResponse<Object>> order(@RequestBody OrderRequestDto orderRequestDto) {
        try {
            OrderResponseDto orderResponseDto = orderService.order(orderRequestDto);

            if (orderResponseDto == null) {
                return ApiResponse.error("Đặt hàng thất bại", HttpStatus.NOT_FOUND);
            }

            // Xử lý thanh toán với PayOS
            if (orderRequestDto.getPaymentMethod() == PaymentMethod.PAYOS.ordinal()) {
                String redirectUrl = paymentService.payment(orderResponseDto.getId(), orderResponseDto.getTotalAmount().intValue());
                orderResponseDto.setRedirectUrl(redirectUrl);

                return ApiResponse.success(orderResponseDto, "Đặt hàng thành công");
            }

            // Xử lý đơn hàng là tiền mặt
            return ApiResponse.success(orderResponseDto, "Đặt hàng thành công");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
