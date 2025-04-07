package com.ecommerce.book_store.service.abstraction;

import com.ecommerce.book_store.http.dto.request.implement.AddressRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.AddressResponseDto;
import com.ecommerce.book_store.persistent.entity.Address;

import java.util.List;

public interface AddressService extends IService<AddressRequestDto, AddressResponseDto, Address> {
    Address addAddress(AddressRequestDto request);
    Address updateAddress(Long addressId, AddressRequestDto request);
    void deleteAddress(Long addressId, Long userId);
    List<Address> getUserAddresses(Long userId);
    Address getDefaultAddress(Long userId);
}
