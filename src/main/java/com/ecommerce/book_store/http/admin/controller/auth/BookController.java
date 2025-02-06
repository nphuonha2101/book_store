package com.ecommerce.book_store.http.admin.controller.auth;

import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    @GetMapping("/{id}")
    public String getProductDetail(@PathVariable Long id, Model model) {
        return bookService.getBookById(id)
                .map(book -> {
                    model.addAttribute("book", book);
                    return "pages/auth/detailBook";
                })
                .orElse("error/404");
    }
}