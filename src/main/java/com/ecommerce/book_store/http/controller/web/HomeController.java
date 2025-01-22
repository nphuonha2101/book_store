package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.http.dto.response.implement.BookResponseDto;
import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.service.abstraction.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final BookService bookService;

    @Autowired
    public HomeController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(path = {"/", "/home"})
    public String index(Model model) {

        model.addAttribute("greet", "Phuong Cute dep gai kkk");
//        model.addAttribute("books", response);
        return "home/index";
    }
}
