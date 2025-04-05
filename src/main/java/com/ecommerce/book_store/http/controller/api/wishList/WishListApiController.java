package com.ecommerce.book_store.http.controller.api.wishList;

import com.ecommerce.book_store.http.ApiResponse;
import com.ecommerce.book_store.http.dto.request.implement.WishListRequestDto;
import com.ecommerce.book_store.persistent.entity.WishList;
import com.ecommerce.book_store.service.abstraction.WishListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/wishlists")
@RestController
public class WishListApiController {
    private final WishListService wishListService;
    public WishListApiController(WishListService wishListService) {this.wishListService = wishListService;}
    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<WishList>>> getAllWishLists(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        try {
            Page<WishList> wishLists = wishListService.findAll(PageRequest.of(page, size));
            return wishLists != null
                ? ApiResponse.successWithPagination(wishLists, "All wish lists fetched")
                : ApiResponse.error("Not found", org.springframework.http.HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<List<WishList>>> getWishListsByUserId(@PathVariable Long userId) {
        try {
            List<WishList> wishLists = wishListService.getWishListByUserId(userId);
            return wishLists != null
                ? ApiResponse.success(wishLists, "Wish lists fetched")
                : ApiResponse.error("Not found", org.springframework.http.HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<WishList>> addWishList(@RequestBody WishListRequestDto request) {
        try {
            WishList added = wishListService.addBookToWishList(request.getUserId(), request.getBookId());
            return added != null
                ? ApiResponse.success(added, "Wish list added")
                : ApiResponse.error("Not found", org.springframework.http.HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<ApiResponse<WishList>> removeWishList(@RequestParam Long userId, @RequestParam Long bookId) {
        try {
          wishListService.removeBookFromWishList(userId, bookId);
            return ApiResponse.success(null, "Wish list removed");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @PostMapping("/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleWishList(@RequestBody WishListRequestDto request) {
        try {
            boolean result = wishListService.toggleWishList(request.getUserId(), request.getBookId());
            return ApiResponse.success(result, "Wish list toggled");
        } catch (Exception e) {
            return ApiResponse.error(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
