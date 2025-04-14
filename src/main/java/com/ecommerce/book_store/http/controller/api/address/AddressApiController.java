package com.ecommerce.book_store.http.controller.api.address;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.AddressRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.AddressResponseDto;
import com.ecommerce.book_store.persistent.entity.Address;
import com.ecommerce.book_store.service.abstraction.AddressService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressApiController {
    private final AddressService addressService;

    public AddressApiController(AddressService addressService) {
        this.addressService = addressService;

    }
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> getAllAddresses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<AddressResponseDto> addresses = addressService.findAll(PageRequest.of(page, size));
            return addresses != null
                    ? ApiResponse.successWithPagination(addresses, "All addresses fetched")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<AddressResponseDto>>> getAddressesByUserId(@PathVariable Long userId) {
        try {
            List<AddressResponseDto> addresses = addressService.getAddressesByUserId(userId);
            return addresses != null
                    ? ApiResponse.success(addresses, "Addresses fetched")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<AddressResponseDto>> addAddress(@RequestBody AddressRequestDto request) {
        try {
            AddressResponseDto added = addressService.addAddress(request);
            return added != null
                    ? ApiResponse.success(added, "Address added")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<AddressResponseDto>> updateAddress(@RequestBody AddressRequestDto request) {
        try {
            AddressResponseDto updated = addressService.updateAddress(request.getAddressId(), request);
            return updated != null
                    ? ApiResponse.success(updated, "Address updated")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Address>> deleteAddress(@RequestParam Long addressId) {
        try {
            addressService.deleteAddress(addressId);
            return ApiResponse.success(null, "Address deleted");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    }
