package com.ecommerce.book_store.http.controller.admin;

import com.ecommerce.book_store.http.dto.request.implement.BookRequestDto;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.Category;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.CategoryService;
import com.ecommerce.book_store.service.implement.CategoryServiceImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookController {

    private final BookService bookService;
    private final CategoryService categoryService;

    public BookController(BookService bookService, CategoryService categoryService) {
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
        return "pages/admin/books/create";
    }

    @PostMapping(value = {"/admin/books/store"})
    public String store(@Valid @ModelAttribute("bookRequestDto") BookRequestDto bookRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return "pages/admin/books/create";
        }

        bookService.save(bookRequestDto);
        return "redirect:/admin/books";
    }

    @PostMapping(value = {"/admin/books/update"})
    public String update() {
        return "pages/admin/books/update";
    }

    @DeleteMapping(value = {"/admin/books/delete"})
    public String delete() {
        return "pages/admin/books/delete";
    }

}
