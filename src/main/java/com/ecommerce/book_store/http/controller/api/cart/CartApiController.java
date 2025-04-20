package com.ecommerce.book_store.http.controller.api.cart;

import com.ecommerce.book_store.core.security.JwtUtils;
import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.CartItemRequestDto;
import com.ecommerce.book_store.http.dto.response.implement.CartItemResponseDto;
import com.ecommerce.book_store.service.abstraction.CartItemService;
import com.ecommerce.book_store.service.abstraction.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RequestMapping("/api/v1/auth/carts")
@RestController
public class CartApiController {
    private final CartItemService cartItemService;
    private final JwtUtils jwtUtils;
    private final UserService userService;

    public CartApiController(CartItemService cartItemService, JwtUtils jwtUtils, UserService userService) {
        this.cartItemService = cartItemService;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CartItemResponseDto>>> getAllCartItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        try {
            Page<CartItemResponseDto> cartItems = cartItemService.findAll(PageRequest.of(page, size));
            return cartItems != null
                    ? ApiResponse.successWithPagination(cartItems, "All cart items fetched")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<List<CartItemResponseDto>>> getCartItemsByUserId(
            @RequestHeader("Authorization") String token
    ) {
        try {
            Long userId = extractUserIdFromToken(token);
            List<CartItemResponseDto> cartItems = cartItemService.getCartItemsByUserId(userId);
            return cartItems != null
                    ? ApiResponse.success(cartItems, "Cart items fetched")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> addCartItem(@RequestBody CartItemRequestDto request) {
        try {
            CartItemResponseDto added = cartItemService.addCartItem(request.getUserId(), request.getBookId(), request.getQuantity());
            return added != null
                    ? ApiResponse.success(added, "Cart item added")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
    }
        catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse<CartItemResponseDto>> updateCartItem(@RequestBody CartItemRequestDto request) {
        try {
            CartItemResponseDto updated = cartItemService.updateCartItem(request.getCartItemId(), request.getQuantity());
            return updated != null
                    ? ApiResponse.success(updated, "Cart item updated")
                    : ApiResponse.error("Not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{cartItemId}")
    public ResponseEntity<ApiResponse<Void>> deleteCartItem(@PathVariable Long cartItemId) {
        try {
            cartItemService.deleteCartItem(cartItemId);
            return ApiResponse.success(null, "Cart item deleted");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> deleteAllCartItems(
            @RequestHeader("Authorization") String token
    ) {
        try {
            Long userId = extractUserIdFromToken(token);
            cartItemService.deleteAllCartItems(userId);
            return ApiResponse.success(null, "All cart items deleted");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Extract user ID from JWT token
     * @param token JWT token containing user's email
     * @return User ID or throws an exception if user not found
     */
    private Long extractUserIdFromToken(String token) {
        String jwtToken = token.substring(7);
        String email = jwtUtils.extractUserEmail(jwtToken);
        Optional<Long> userOptional = userService.findIdByEmail(email);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found"));
    }
}