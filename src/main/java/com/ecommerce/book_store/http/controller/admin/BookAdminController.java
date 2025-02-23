package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
public class BookAdminController {

    private final BookService bookService;
    private final CategoryService categoryService;

    public BookAdminController(BookService bookService, CategoryService categoryService) {
        this.bookService = bookService;
        this.categoryService = categoryService;
    }

    @GetMapping(value = {"/admin/books", "/admin/books/index"})
    public String index(Model model, Pageable pageable) {
        Page<Book> page = bookService.findAll(pageable);

        model.addAttribute("page", page);
        model.addAttribute("url", "/admin/books");
        model.addAttribute("CONTENT_TITLE", "Quản lý sách");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/books/index";
    }

    @GetMapping(value = {"/admin/books/create"})
    public String create(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        model.addAttribute("CONTENT_TITLE", "Thêm sách");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/books/create";
    }

    @GetMapping(value = {"/admin/books/edit/{id}"})
    public String edit(@PathVariable Long id, Model model) {
        Book book = bookService.findById(id);
        List<Category> categories = categoryService.findAll();
        model.addAttribute("book", book);
        model.addAttribute("categories", categories);
        model.addAttribute("CONTENT_TITLE", "Chỉnh sửa sách");
        model.addAttribute("LAYOUT_TITLE", "Admin BookStore");
        return "pages/admin/books/edit";
    }

    @PostMapping(value = {"/admin/books/store"})
    public String store(@Valid @ModelAttribute("bookRequestDto") BookRequestDto bookRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "pages/admin/books/create";
            }

            bookService.save(bookRequestDto);
            redirectAttributes.addFlashAttribute("success", "Thêm sách thành công");
            return "redirect:/admin/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Thêm sách thất bại");
            return "redirect:/admin/books/create";
        }
    }

    @PostMapping(value = {"/admin/books/{bookId}/update"})
    public String update(@PathVariable Long bookId, @Valid @ModelAttribute("bookRequestDto") BookRequestDto bookRequestDto, BindingResult result, RedirectAttributes redirectAttributes) {
        try {
            if (result.hasErrors()) {
                return "pages/admin/books/edit";
            }

            bookService.update(bookRequestDto, bookId);
            redirectAttributes.addFlashAttribute("success", "Cập nhật sách thành công");
            return "redirect:/admin/books";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Cập nhật sách thất bại");
            return "redirect:/admin/books/edit/" + bookId;
        }
    }

    @DeleteMapping(value = {"/admin/books/delete/{id}"})
    @ResponseBody
    public Map<String, Object> delete(@PathVariable Long id) {
        try {
            bookService.deleteById(id);
            return Map.of("message", "Xóa sách thành công", "status", 200, "success", true);
        } catch (Exception e) {
            return Map.of("message", "Xóa sách thất bại", "status", 500, "success", false);
        }
    }

}
