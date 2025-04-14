package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.AddressRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.AddressResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Address;
import com.ecommerce.book_store.persistent.repository.abstraction.AddressRepository;
import com.ecommerce.book_store.service.abstraction.AddressService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl extends IServiceImpl<AddressRequestDto, AddressResponseDto, Address>
        implements AddressService {
    private final AddressRepository addressRepository;

   public AddressServiceImpl(AddressRepository addressRepository) {
       super(addressRepository);
       this.addressRepository = addressRepository;
    }
    @Override
    public Address toEntity(AddressRequestDto requestDto) {
        Address address = new Address();
        address.setFullName(requestDto.getFullName());
        address.setPhone(requestDto.getPhone());
        address.setProvince(requestDto.getProvince());
        address.setDistrict(requestDto.getDistrict());
        address.setWard(requestDto.getWard());
        address.setAddInfo(requestDto.getAddInfo());
        address.setDefault(requestDto.getIsDefault());
        address.setUserId(requestDto.getUserId());
        return address;
    }

    @Override
    public AddressResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        Address address = (Address) entity;
        return new AddressResponseDto(
                address.getId(),
                address.getUserId(),
                address.getFullName() != null ? address.getFullName() : "",
                address.getPhone() != null ? address.getPhone() : "",
                address.getProvince() != null ? address.getProvince() : "",
                address.getDistrict() != null ? address.getDistrict() : "",
                address.getWard() != null ? address.getWard() : "",
                address.getAddInfo() != null ? address.getAddInfo() : "",
                address.isDefault());
    }
    @Override
    public void copyProperties(AddressRequestDto requestDto, Address entity) {
       if (requestDto == null || entity == null) {
            return;
        }

       if (requestDto.getUserId() != null) {
            entity.setUserId(requestDto.getUserId());
        }

       if (requestDto.getFullName() != null) {
            entity.setFullName(requestDto.getFullName());
        }

        if (requestDto.getPhone() != null) {
            entity.setPhone(requestDto.getPhone());
        }

       if (requestDto.getProvince() != null) {
            entity.setProvince(requestDto.getProvince());
        }

        if (requestDto.getDistrict() != null) {
            entity.setDistrict(requestDto.getDistrict());
        }

        if (requestDto.getWard() != null) {
            entity.setWard(requestDto.getWard());
        }

        if (requestDto.getAddInfo() != null) {
            entity.setAddInfo(requestDto.getAddInfo());
        }

        if (requestDto.getIsDefault() != null) {
            entity.setDefault(requestDto.getIsDefault());
        }

    }

    @Override
    public AddressResponseDto addAddress(AddressRequestDto request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("User ID is required");
        }
        // Nếu địa chỉ mới được đặt làm mặc định, bỏ chọn mặc định của các địa chỉ khác
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.updateDefaultAddressStatus(request.getUserId(), null);
        }
        Address address = toEntity(request);
        return toResponseDto(addressRepository.save(address));
    }

    @Override
    public AddressResponseDto updateAddress(Long addressId, AddressRequestDto request) {
        Optional<Address> existingAddress = addressRepository.findById(addressId);
        if (existingAddress.isEmpty()) {
            throw new RuntimeException("Address not found");
        }
        Address address = existingAddress.get();
        // Nếu địa chỉ mới được đặt làm mặc định, bỏ chọn mặc định của các địa chỉ khác
        if (Boolean.TRUE.equals(request.getIsDefault())) {
            addressRepository.updateDefaultAddressStatus(address.getUserId(), addressId);
        }
        copyProperties(request, address);
        return toResponseDto(addressRepository.save(address));
    }
    @Override
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        addressRepository.deleteById(addressId);
    }

    @Override
    public List<AddressResponseDto> getAddressesByUserId(Long userId) {
        return addressRepository.findByUserId(userId)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }
}
