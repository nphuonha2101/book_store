package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.BookImageRequestDto;
import com.ecommerce.book_store.persistent.entity.BookImage;
import com.ecommerce.book_store.service.abstraction.BookImageService;
import groovy.util.logging.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
@Slf4j
@Log4j
@Controller
public class BookImageAdminController {
    private final BookImageService bookImageService;

    public BookImageAdminController(BookImageService bookImageService) {
        this.bookImageService = bookImageService;
    }

    @GetMapping(value = {"/admin/books/{bookId}/images", "/admin/books/{bookId}/images/index"})
    public String index(@PathVariable Long bookId, Model model, Pageable pageable) {
        try {
            System.out.println("bookId: " + bookId);
            Page<BookImage> page = bookImageService.findAllByBookId(bookId, pageable);

            model.addAttribute("page", page);
            model.addAttribute("url", "/admin/book/" + bookId + "/images");
            model.addAttribute("bookId", bookId);
            model.addAttribute("CONTENT_TITLE", "Quản lý ảnh sách");
            model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
            return "pages/admin/books/images/index";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "redirect:/admin/books";
        }

    }

    @PostMapping("/admin/books/{bookId}/images")
    @ResponseBody
    public Map<String, Object> store(@ModelAttribute("bookImageRequestDto")BookImageRequestDto bookImageRequestDto) {
        try {
            bookImageService.save(bookImageRequestDto);
            return Map.of("message", "Thêm ảnh sách thành công", "status", 200);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Map.of("message", "Thêm ảnh sách thất bại", "status", 500);
        }
    }

    @DeleteMapping("/admin/books/{bookId}/images/{id}")
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Long bookId, @PathVariable Long id) {
        try {
            bookImageService.deleteById(id);
            return Map.of("message", "Xóa ảnh sách thành công", "status", 200);
        } catch (Exception e) {
            return Map.of("message", "Xóa ảnh sách thất bại", "status", 500);
        }
    }
}
