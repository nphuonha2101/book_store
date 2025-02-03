package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final BookService bookService;

    @Autowired
    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = {"/", "/home"})
    public String index(Model model, Pageable pageable) {
        model.addAttribute("CONTENT_TITLE", "Trang chủ");
        model.addAttribute("LAYOUT_TITLE", "BookStore");
        Page<Book> page = bookService.findAll(pageable);
        model.addAttribute("page", page);
        return "pages/home/index";
    }
}
