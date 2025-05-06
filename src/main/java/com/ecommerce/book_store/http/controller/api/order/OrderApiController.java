package com.ecommerce.book_store.http.controller.api.order;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.core.constant.PaymentMethod;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.persistent.entity.Order;
import com.ecommerce.book_store.service.abstraction.OrderService;
import com.ecommerce.book_store.service.abstraction.PaymentService;
import com.ecommerce.book_store.service.abstraction.UserService;
import com.google.api.gax.rpc.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/v1/auth/orders")
@RestController
public class OrderApiController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public OrderApiController(OrderService orderService, JwtUtils jwtUtils, UserService userService, @Qualifier("payOSPaymentService") PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
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
                orderResponseDto.setPaymentUrl(redirectUrl);

                return ApiResponse.success(orderResponseDto, "Đặt hàng thành công");
            }

            // Xử lý đơn hàng là tiền mặt
            return ApiResponse.success(orderResponseDto, "Đặt hàng thành công");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<OrderResponseDto>>> getOrderHistory(
            @RequestParam(required = false) String status,
            @RequestHeader("Authorization") String token
    ) {
        try {
            String jwtToken = token.substring(7);
            String email = jwtUtils.extractUserEmail(jwtToken);
            Long userId = userService.findIdByEmail(email).orElseThrow(
                    () -> new RuntimeException("User not found")
            );

            OrderStatus orderStatus = null;
            if (status != null && !status.isEmpty()) {
                orderStatus = OrderStatus.valueOf(status.toUpperCase());
            }

            List<OrderResponseDto> orderHistory = orderService.getOrderHistory(userId, orderStatus);
            return ApiResponse.success(orderHistory, "Lấy danh sách đơn hàng thành công");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra khi lấy danh sách đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/cancel")
    public ResponseEntity<ApiResponse<OrderResponseDto>> cancelOrder(
            @RequestParam Long orderId,
            @RequestParam String cancellationReason,
            @RequestHeader("Authorization") String token) {
        try {
            OrderResponseDto order = orderService.cancelOrder(orderId, cancellationReason);
            return order != null ? ApiResponse.success(order, "Hủy đơn hàng thành công") : ApiResponse.error("Hủy đơn hàng thất bại", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponseDto>> getOrderById(@PathVariable("orderId") Long orderId) {
        try {
            OrderResponseDto orderResponseDto = orderService.findById(orderId);
            if (orderResponseDto == null) {
                return ApiResponse.error("Đơn hàng không tồn tại", HttpStatus.NOT_FOUND);
            }
            return ApiResponse.success(orderResponseDto, "Lấy thông tin đơn hàng thành công");
        } catch (NotFoundException e) {
            return ApiResponse.error("Đơn hàng không tồn tại", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
          return ApiResponse.error("Có lỗi xảy ra khi lấy thông tin đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/make-failed/{orderId}")
    public ResponseEntity<ApiResponse<Object>> makeFailed(@PathVariable Long orderId) {
        try {
            orderService.updateOrderStatus(orderId, OrderStatus.FAILED);
            return ApiResponse.success(null, "Cập nhật trạng thái đơn hàng thành công");
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.error("Có lỗi xảy ra", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}