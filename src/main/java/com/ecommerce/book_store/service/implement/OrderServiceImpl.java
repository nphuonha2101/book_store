package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.core.constant.OrderStatus;
import com.ecommerce.book_store.http.dto.request.implement.OrderRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.OrderResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.UserResponseDto;
import com.ecommerce.book_store.http.dto.response.implement.VoucherResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Order;
import com.ecommerce.book_store.persistent.entity.User;
import com.ecommerce.book_store.persistent.entity.Voucher;
import com.ecommerce.book_store.persistent.repository.abstraction.OrderRepository;
import com.ecommerce.book_store.service.abstraction.OrderService;
import com.ecommerce.book_store.service.abstraction.RoleService;
import com.ecommerce.book_store.service.abstraction.UserService;
import com.ecommerce.book_store.service.abstraction.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends IServiceImpl<OrderRequestDto, OrderResponseDto, Order>
        implements OrderService {
    private final VoucherService voucherService;
    private final UserService userService;

    @Autowired
    public OrderServiceImpl(OrderRepository repository, VoucherService voucherService, UserService userService) {
        super(repository);
        this.voucherService = voucherService;
        this.userService = userService;
    }

    @Override
    public Order toEntity(OrderRequestDto requestDto) {
        Order orderResult = new Order(
                null,
                null,
                requestDto.getAddress(),
                requestDto.getPhone(),
                requestDto.getNote(),
                OrderStatus.valueOf(requestDto.getStatus().toUpperCase())
        );
        User user = userService.findById(requestDto.getUserId());
        Voucher voucher = voucherService.findById(requestDto.getVoucherId());

        orderResult.setUser(user);
        orderResult.setVoucher(voucher);

        return orderResult;
    }

    @Override
    public OrderResponseDto toResponseDto(AbstractEntity entity) {
        Order order = (Order) entity;
        UserResponseDto userResponseDto = userService.toResponseDto(order.getUser());
        VoucherResponseDto voucherResponseDto= voucherService.toResponseDto(order.getVoucher());
        return new OrderResponseDto(
                order.getId(),
                userResponseDto,
                voucherResponseDto,
                order.getAddress(),
                order.getPhone(),
                order.getNote(),
                String.valueOf(order.getStatus()),
                order.getTotalAmount()

                );
    }

    @Override
    public void copyProperties(OrderRequestDto requestDto, Order entity) {
        User user = userService.findById(requestDto.getUserId());
        Voucher voucher = voucherService.findById(requestDto.getVoucherId());
        entity.setUser(user);
        entity.setVoucher(voucher);
        entity.setAddress(requestDto.getAddress());
        entity.setPhone(requestDto.getPhone());
        entity.setNote(requestDto.getNote());
        entity.setStatus(OrderStatus.valueOf(requestDto.getStatus()));
    }
}
