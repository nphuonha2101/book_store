package com.ecommerce.book_store.http.controller.api.cart;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.CartItemRequestDto;
import com.ecommerce.book_store.persistent.entity.CartItem;
import com.ecommerce.book_store.service.abstraction.CartItemService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/carts")
@RestController
public class CartApiController {
    private final CartItemService cartItemService;

    public CartApiController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<CartItem>>> getAllCartItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<CartItem> cartItems = cartItemService.findAll(PageRequest.of(page, size));
            return cartItems != null
                    ? ApiResponse.successWithPagination(cartItems, "All cart items fetched")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<CartItem>>> getCartItemsByUserId(@PathVariable Long userId) {
        try {
            List<CartItem> cartItems = cartItemService.getCartItemsByUserId(userId);
            return cartItems != null
                    ? ApiResponse.success(cartItems, "Cart items fetched")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItem>> addCartItem(@RequestBody CartItemRequestDto request) {
        try {
            CartItem added = cartItemService.addCartItem(request.getUserId(), request.getBookId(), request.getQuantity());
            return added != null
                    ? ApiResponse.success(added, "Cart item added")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
    }
        catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<CartItem>> updateCartItem(@RequestBody CartItemRequestDto request) {
        try {
            CartItem updated = cartItemService.updateCartItem(request.getUserId(), request.getBookId(), request.getQuantity());
            return updated != null
                    ? ApiResponse.success(updated, "Cart item updated")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(
            @RequestParam Long userId,
            @RequestParam Long bookId) {
        try {
            cartItemService.deleteCartItem(userId, bookId);
            return ApiResponse.success(null, "Cart item deleted");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}