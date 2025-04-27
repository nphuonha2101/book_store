package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.AddressRequestDto;
import com.ecommerce.book_store.http.dto.request.implement.OrderItemRequestDto;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.*;
import com.ecommerce.book_store.persistent.entity.*;
import com.ecommerce.book_store.persistent.repository.abstraction.OrderRepository;
import com.ecommerce.book_store.persistent.repository.abstraction.UserRepository;
import com.ecommerce.book_store.service.abstraction.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl extends IServiceImpl<OrderRequestDto, OrderResponseDto, Order>
        implements OrderService {
    private final VoucherService voucherService;
    private final UserService userService;
    private final AddressService addressService;
    private final OrderItemService orderItemService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, VoucherService voucherService, UserService userService, AddressService addressService, @Lazy OrderItemService orderItemService) {
        super(repository);
        this.voucherService = voucherService;
        this.userService = userService;
        this.addressService = addressService;
        this.orderItemService = orderItemService;
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
        VoucherResponseDto voucherResponseDto= voucherService.toResponseDto(order.getVoucher());
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
                order.getCreatedAt()
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

            return toResponseDto(order);
        } catch (Exception e) {
            throw new Exception("Error while processing order: " + e.getMessage());
        }
    }

}
