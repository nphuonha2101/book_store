package com.ecommerce.book_store.http.controller.web;

import com.ecommerce.book_store.persistent.entity.Book;
import com.ecommerce.book_store.persistent.entity.Slider;
import com.ecommerce.book_store.service.abstraction.BookService;
import com.ecommerce.book_store.service.abstraction.SliderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final BookService bookService;
    private final SliderService sliderService;


    @Autowired
    public HomeController(BookService bookService, SliderService sliderService) {
        this.bookService = bookService;
        this.sliderService = sliderService;
    }

    @GetMapping(path = {"/", "/home"})
    public String index(Model model, Pageable pageable) {
        model.addAttribute("CONTENT_TITLE", "Trang chủ");
        model.addAttribute("LAYOUT_TITLE", "BookStore");
        Page<Book> page = bookService.findAll(pageable);
        model.addAttribute("page", page);
        List<Slider> sliders = sliderService.findAll();
        model.addAttribute("sliders", sliders);
        return "pages/home/index";
    }
}
