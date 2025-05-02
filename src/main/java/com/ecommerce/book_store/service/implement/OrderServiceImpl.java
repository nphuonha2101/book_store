package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.OrderItemRequestDto;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.*;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.OrderRepository;
import com.ecommerce.book_store.service.abstraction.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class OrderServiceImpl extends IServiceImpl<OrderRequestDto, OrderResponseDto, Order>
        implements OrderService {
    private final VoucherService voucherService;
    private final UserService userService;
    private final AddressService addressService;
    private final OrderItemService orderItemService;
    private final NotificationService notificationService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, VoucherService voucherService, UserService userService, AddressService addressService, @Lazy OrderItemService orderItemService, NotificationService notificationService) {
        super(repository);
        this.voucherService = voucherService;
        this.userService = userService;
        this.addressService = addressService;
        this.orderItemService = orderItemService;
        this.notificationService = notificationService;
    }

    @Override
    public Order toEntity(OrderRequestDto requestDto) {
        Order orderResult = new Order(
                null,
                null,
                null,
                requestDto.getPhone(),
                requestDto.getNote(),
                null,
                requestDto.getTotalAmount(),
                requestDto.getTotalDiscount(),
                requestDto.getShippingFee(),
                requestDto.getPaymentMethod(),
                requestDto.getCancellationReason()
        );

        if (requestDto.getStatus() != null && !requestDto.getStatus().isEmpty()) {
            orderResult.setStatus(OrderStatus.valueOf(requestDto.getStatus()));
        }

        Address address = addressService.getRepository().findById(requestDto.getAddressId()).orElseThrow(
                () -> new RuntimeException("Address not found")
        );

        User user = userService.getRepository().findById(requestDto.getUserId()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Voucher voucher = voucherService.getRepository().findById(requestDto.getVoucherId()).orElse(null);
        orderResult.setUser(user);
        orderResult.setVoucher(voucher);
        orderResult.setAddress(address);
        orderResult.setPaymentMethod(requestDto.getPaymentMethod());

        return orderResult;
    }

    @Override
    public OrderResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }

        Order order = (Order) entity;
        UserResponseDto userResponseDto = userService.toResponseDto(order.getUser());
        VoucherResponseDto voucherResponseDto = voucherService.toResponseDto(order.getVoucher());
        AddressResponseDto addressResponseDto = addressService.toResponseDto(order.getAddress());

        List<OrderItemResponseDto> orderItemResponseDtos = null;
        if (order.getOrderItems() != null) {
            orderItemResponseDtos = order.getOrderItems().stream()
                    .map(orderItemService::toResponseDto)
                    .toList();
        }

        return new OrderResponseDto(
                order.getId(),
                userResponseDto,
                voucherResponseDto,
                addressResponseDto,
                order.getPhone(),
                order.getNote(),
                String.valueOf(order.getStatus()),
                order.getTotalAmount(),
                order.getTotalDiscount(),
                orderItemResponseDtos,
                order.getPaymentMethod().ordinal(),
                order.getCancellationReason(),
                order.getCreatedAt(),
                order.getShippingFee()
        );
    }

    @Override
    public void copyProperties(OrderRequestDto requestDto, Order entity) {
        if (requestDto.getAddressId() != null) {
            Address address = addressService.getRepository().findById(requestDto.getAddressId()).orElseThrow(
                    () -> new RuntimeException("Address not found")
            );
            entity.setAddress(address);
        }

        if (requestDto.getTotalAmount() != 0) {
            entity.setTotalAmount(requestDto.getTotalAmount());
        }

        if (requestDto.getTotalDiscount() != 0) {
            entity.setTotalDiscount(requestDto.getTotalDiscount());
        }

        if (requestDto.getPhone() != null) {
            entity.setPhone(requestDto.getPhone());
        }

        if (requestDto.getNote() != null) {
            entity.setNote(requestDto.getNote());
        }

        if (requestDto.getUserId() != null) {
            User user = userService.getRepository().findById(requestDto.getUserId()).orElseThrow(
                    () -> new RuntimeException("User not found")
            );
            entity.setUser(user);
        }

        if (requestDto.getVoucherId() != null) {
            Voucher voucher = voucherService.getRepository().findById(requestDto.getVoucherId()).orElseThrow(
                    () -> new RuntimeException("Voucher not found")
            );
            entity.setVoucher(voucher);
        }

        if (requestDto.getPaymentMethod() != null) {
            entity.setPaymentMethod(requestDto.getPaymentMethod());
        }

        if (requestDto.getShippingFee() != 0) {
            entity.setShippingFee(requestDto.getShippingFee());
        }
    }

    @Override
    @Transactional
    public OrderResponseDto order(OrderRequestDto orderRequestDto) throws Exception {
        try {
            Order order = toEntity(orderRequestDto);
            order.setStatus(OrderStatus.PENDING);
            order = getRepository().save(order);

            List<OrderItemRequestDto> orderItemRequestDtos = orderRequestDto.getOrderItems();
            for (OrderItemRequestDto orderItemRequestDto : orderItemRequestDtos) {
                OrderItem orderItem = orderItemService.toEntity(orderItemRequestDto);
                orderItem.setOrder(order);
                orderItemService.getRepository().save(orderItem);
            }

            // Create notification
            notificationService.sendNotificationToUser(
                    "Đơn hàng mới đã được tạo",
                    "Đơn hàng của bạn đã được tạo thành công. Vui lòng kiểm tra thông tin đơn hàng trong tài khoản của bạn.",
                    order.getUser().getId(),
                    "/order/" + order.getId()
            );

            return toResponseDto(order);
        } catch (Exception e) {
            throw new Exception("Error while processing order: " + e.getMessage());
        }
    }

    @Transactional
    public void updateOrderStatus(Long orderId, OrderStatus newStatus) {
        try {
            Order order = this.getRepository().findById(orderId)
                    .orElseThrow(() -> new IllegalArgumentException("Order not found"));

            if (!OrderStatusTransitionManager.isValidTransition(order.getStatus(), newStatus)) {
                throw new IllegalStateException("Invalid status transition from " + order.getStatus() + " to " + newStatus);
            }

            if (newStatus == OrderStatus.CANCELLED) {
                order.setCancellationReason("Đơn hàng đã bị huỷ bởi hệ thống");
                notificationService.sendNotificationToUser(
                        "Đơn hàng đã bị huỷ",
                        "Đơn hàng #" + order.getId() +  " của bạn đã bị huỷ. Vui lòng kiểm tra lại thông tin đơn hàng trong tài khoản của bạn.",
                        order.getUser().getId(),
                        "/order/" + order.getId()
                );

            } else if (newStatus == OrderStatus.DELIVERED) {
                notificationService.sendNotificationToUser(
                        "Đơn hàng đã được giao",
                        "Đơn hàng #" + order.getId() + " của bạn đã được giao thành công. Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi.",
                        order.getUser().getId(),
                        "/order/" + order.getId()
                );
            } else if (newStatus == OrderStatus.SHIPPING) {
                notificationService.sendNotificationToUser(
                        "Đơn hàng đang được giao",
                        "Đơn hàng #" + order.getId() + " của bạn đang được giao. Vui lòng kiểm tra thông tin đơn hàng trong tài khoản của bạn.",
                        order.getUser().getId(),
                        "/order/" + order.getId()
                );
            } else if (newStatus == OrderStatus.PROCESSING) {
                notificationService.sendNotificationToUser(
                        "Đơn hàng đang được xử lý",
                        "Đơn hàng #" + order.getId() + " của bạn đang được xử lý. Vui lòng kiểm tra thông tin đơn hàng trong tài khoản của bạn.",
                        order.getUser().getId(),
                        "/order/" + order.getId()
                );
            }

            order.setStatus(newStatus);
            this.getRepository().save(order);
        } catch (Exception e) {
            log.error("Error while updating order status: {}", e.getMessage());
            throw new RuntimeException("Error while updating order status: " + e.getMessage());
        }
    }

    public List<OrderStatus> getAvailableStatuses(Long orderId) {
        Order order = this.getRepository().findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return OrderStatusTransitionManager.getAllowedTransitions(order.getStatus());
    }

    @Override
    public List<OrderResponseDto> getOrderHistory(Long userId) {
        return ((OrderRepository) getRepository()).findById(userId).stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public void cancelOrder(Long orderId, Long userId) {
        Order order = ((OrderRepository) getRepository()).findByIdAndUserId(orderId, userId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found for the given user.");
        }
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Order is already cancelled.");
        }
        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason("Order cancelled by user.");
        getRepository().save(order);
    }

}
