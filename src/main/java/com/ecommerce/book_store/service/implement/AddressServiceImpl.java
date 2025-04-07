package com.ecommerce.book_store.service.implement;

import com.ecommerce.book_store.http.dto.request.implement.AddressRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.AddressResponseDto;
import com.ecommerce.book_store.persistent.entity.AbstractEntity;
import com.ecommerce.book_store.persistent.entity.Address;
import com.ecommerce.book_store.persistent.repository.abstraction.AddressRepository;
import com.ecommerce.book_store.service.abstraction.AddressService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class AddressServiceImpl extends IServiceImpl<AddressRequestDto, AddressResponseDto, Address>
        implements AddressService {
    private final AddressRepository addressRepository;

   public AddressServiceImpl(AddressRepository addressRepository) {
       super(addressRepository);
       this.addressRepository = addressRepository;
    }
    @Override
    public Address addAddress(AddressRequestDto request) {
        if (request.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findByUserId(request.getUserId());
            existingAddresses.forEach(address -> {
                if (address.isDefault()) {
                    address.setDefault(false);
                    addressRepository.save(address);
                }
            });
        }
        Address address = new Address();
        address.setUserId(request.getUserId());
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setDistrict(request.getDistrict());
        address.setWard(request.getWard());
        address.setAddInfo(request.getAddInfo());
        address.setDefault(request.getIsDefault());
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(Long addressId, AddressRequestDto request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        // Kiểm tra quyền sở hữu
        if (!address.getUserId().equals(request.getUserId())) {
            throw new RuntimeException("You are not authorized to update this address");
        }

        // Nếu cập nhật thành địa chỉ mặc định, bỏ chọn mặc định của các địa chỉ khác
        if (request.getIsDefault()) {
            List<Address> existingAddresses = addressRepository.findByUserId(request.getUserId());
            existingAddresses.forEach(addr -> {
                if (addr.isDefault() && !addr.getId().equals(addressId)) {
                    addr.setDefault(false);
                    addressRepository.save(addr);
                }
            });
        }
        address.setFullName(request.getFullName());
        address.setPhone(request.getPhone());
        address.setProvince(request.getProvince());
        address.setDistrict(request.getDistrict());
        address.setWard(request.getWard());
        address.setAddInfo(request.getAddInfo());
        address.setDefault(request.getIsDefault());

        return addressRepository.save(address);

    }

    @Override
    public void deleteAddress(Long addressId, Long userId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));
        // Kiểm tra quyền sở hữu
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("You are not authorized to delete this address");
        }
        addressRepository.delete(address);
    }

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Override
    public Address getDefaultAddress(Long userId) {
        Address defaultAddress = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        if (defaultAddress == null) {
            throw new RuntimeException("No default address found for user");
        }
        return defaultAddress;
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
        return address;
    }

    @Override
    public AddressResponseDto toResponseDto(AbstractEntity entity) {
        if (entity == null) {
            return null;
        }
        Address address = (Address) entity;
        return new AddressResponseDto(address.getId(), address.getUserId(), address.getFullName(),
                address.getPhone(), address.getProvince(), address.getDistrict(), address.getWard(),
                address.getAddInfo(), address.isDefault());
    }

    @Override
    public void copyProperties(AddressRequestDto requestDto, Address entity) {
        entity.setFullName(requestDto.getFullName());
        entity.setPhone(requestDto.getPhone());
        entity.setProvince(requestDto.getProvince());
        entity.setDistrict(requestDto.getDistrict());
        entity.setWard(requestDto.getWard());
        entity.setAddInfo(requestDto.getAddInfo());

    }
}
